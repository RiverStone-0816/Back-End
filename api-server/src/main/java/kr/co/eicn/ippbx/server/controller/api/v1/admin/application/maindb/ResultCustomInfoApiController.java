package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.maindb;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.controller.api.v1.admin.record.history.RecordApiController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.ResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.ResultCustomInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.MaindbGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.EicnCdrService;
import kr.co.eicn.ippbx.server.service.RecordFileService;
import kr.co.eicn.ippbx.server.service.ResultCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/maindb/resultcustominfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ResultCustomInfoApiController extends ApiBaseController {

    private final ResultCustomInfoService service;
    private final MaindbGroupRepository maindbGroupRepository;
    private final RecordFileService recordFileService;
    private final EicnCdrService eicnCdrService;
    private final RecordApiController recordApiController;
    private final MaindbCustomInfoApiController maindbCustomInfoApiController;
    private final PersonListRepository personListRepository;

    //고객DB목록조회
    @GetMapping("customdb_group")
    public ResponseEntity<JsonResult<List<SearchMaindbGroupResponse>>> customdb_group() {
        final List<SearchMaindbGroupResponse> list = maindbGroupRepository.findAll().stream()
                .map((e) -> convertDto(e, SearchMaindbGroupResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<ResultCustomInfoEntity>>> getPagination(ResultCustomInfoSearchRequest search) {
        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, e -> e));
        final Pagination<ResultCustomInfoEntity> pagination = service.getRepository().pagination(search);
        final List<ResultCustomInfoEntity> rows = pagination.getRows().stream()
                .map((e) -> {
                    final ResultCustomInfoEntity response = convertDto(e, ResultCustomInfoEntity.class);

                    if (Objects.nonNull(personListMap.get(response.getUserid())))
                        response.setPersonList(personListMap.get(response.getUserid()));

                    return response;
                })
                .collect(toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{groupSeq}/data")
    public ResponseEntity<JsonResult<Pagination<ResultCustomInfoEntity>>> getPagination(@PathVariable Integer groupSeq, ResultCustomInfoSearchRequest search) {
        search.setSeq(groupSeq);

        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, e -> e));
        final Pagination<ResultCustomInfoEntity> pagination = service.getRepository().pagination(search);
        final List<ResultCustomInfoEntity> rows = pagination.getRows().stream()
                .map((e) -> {
                    final ResultCustomInfoEntity response = convertDto(e, ResultCustomInfoEntity.class);

                    if (Objects.nonNull(personListMap.get(response.getUserid())))
                        response.setPersonList(personListMap.get(response.getUserid()));

                    return response;
                })
                .collect(toList());


        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    //수정정보SEQ
    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<ResultCustomInfoEntity>> get(@PathVariable Integer seq) {
        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, e -> e));
        ResultCustomInfoEntity response = service.getRepository().findOneIfNullThrow(seq);
        if(Objects.nonNull(personListMap.get(response.getUserid())))
            response.setPersonList(personListMap.get(response.getUserid()));

        return ResponseEntity.ok(data(convertDto(response, ResultCustomInfoEntity.class)));
    }

    //수정정보SEQ
    @GetMapping("{userId}/{phone}")
    public ResponseEntity<JsonResult<List<ResultCustomInfoEntity>>> getTodo(@PathVariable String userId, @PathVariable String phone) {
        List<ResultCustomInfoEntity> response = service.getRepository().getTodo(userId, phone);
        if (response.size() > 0 && StringUtils.isNotEmpty(response.get(0).getUserid())) {
            ResultCustomInfoEntity row = response.get(0);

            row.setPersonList(personListRepository.findOneById(response.get(0).getUserid()));
        }

        return ResponseEntity.ok(data(response));
    }

    //수정
    @PutMapping("{seq}")
    public JsonResult<Void> put(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult,
                                @PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        ResultCustomInfoEntity response = service.getRepository().findOneIfNullThrow(seq);
        if (g.getUser().getIdType().equals(IdType.USER.getCode()) && !g.getUser().getId().equals(response.getUserid()))
            if(form.getTodoSequences().size() == 0)
                throw new IllegalArgumentException("다른 상담원의 이력은 수정할 수 없습니다.");

        service.getRepository().update(form, seq);
        return create();
    }

    //삭제
    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> deleteData(@PathVariable Integer seq) {
        ResultCustomInfoEntity response = service.getRepository().findOneIfNullThrow(seq);
        if (g.getUser().getIdType().equals(IdType.USER.getCode()) && !g.getUser().getId().equals(response.getUserid()))
            throw new IllegalArgumentException("다른 상담원의 이력은 삭제할 수 없습니다.");

        service.getRepository().delete(seq);
        return ResponseEntity.ok(create());
    }

    @GetMapping("{uniqueId}/record-files")
    public ResponseEntity<JsonResult<List<RecordFile>>> getFiles(@PathVariable String uniqueId) {
        return ResponseEntity.ok().body(data(recordFileService.fetchAll(eicnCdrService.getRepository().findAllByUniqueId(uniqueId))));
    }

    @GetMapping(value = "resource", params = {"token"})
    public ResponseEntity<Resource> resource(@RequestParam("path") String recordFile/*파일명을 포함한 파일경로*/, @RequestParam("mode") String mode) {
        return recordApiController.resource(recordFile, mode);
    }

    //추가
    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final ResultCustomInfoRepository repository = service.getRepository();

        final ResultCustomInfoSearchRequest search = new ResultCustomInfoSearchRequest();

        if(form.getClickKey() != null && !form.getGroupKind().equals("TALK"))
            search.setClickKey(form.getClickKey());

        final List<ResultCustomInfoEntity> seqCheck = repository.getOne(search);

        if (seqCheck.size() > 0) {
            repository.update(form, seqCheck.get(0).getSeq());
        } else {
            repository.insert(form);
        }

        return ResponseEntity.created(URI.create("api/v1/admin/application/maindb/resultcustominfo_add")).body(create());
    }

    @GetMapping(value = "file-resource", params = {"token"})
    public ResponseEntity<Resource> specificFileResource(@RequestParam String file) {
        return maindbCustomInfoApiController.specificFileResource(file);
    }
}
