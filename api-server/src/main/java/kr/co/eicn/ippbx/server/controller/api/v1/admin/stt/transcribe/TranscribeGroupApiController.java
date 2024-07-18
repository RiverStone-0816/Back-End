package kr.co.eicn.ippbx.server.controller.api.v1.admin.stt.transcribe;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeGroupEntity;
import kr.co.eicn.ippbx.model.form.TranscribeFileFormRequest;
import kr.co.eicn.ippbx.model.form.TranscribeGroupFormRequest;
import kr.co.eicn.ippbx.model.search.TranscribeGroupSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.TranscribeGroupService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * STT > 전사툴 > 그룹관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stt/transcribe/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class TranscribeGroupApiController extends ApiBaseController {

    private final TranscribeGroupService transcribeGroupService;
    private final PersonListRepository personListRepository;

    /**
     *
     * 전사툴 그룹 목록조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<TranscribeGroupResponse>>> pagination(TranscribeGroupSearchRequest search) {
        Pagination<TranscribeGroupEntity> pagination;
        List<PersonList> personList = personListRepository.findAll();

        pagination = transcribeGroupService.getRepository().pagination(search);
        List<TranscribeGroupResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    TranscribeGroupResponse transcribeGroupResponse = convertDto(e, TranscribeGroupResponse.class);

                    transcribeGroupResponse.setUserId(personList.stream().filter(person -> person.getId().equals(e.getUserId())).map(PersonList::getIdName).findFirst().orElse(""));

                    return transcribeGroupResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{seq}")
    public ResponseEntity<JsonResult<TranscribeGroupResponse>> get(@PathVariable Integer seq) {
        final TranscribeGroupResponse detail = convertDto(transcribeGroupService.getRepository().findOneIfNullThrow(seq), TranscribeGroupResponse.class);

        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, e -> e));
        final List<PersonList> personList = personListRepository.findAll();

        return ResponseEntity.ok().body(data(detail));
    }

    @PostMapping(value = "")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody TranscribeGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        //TranscribeGroupService.insertOnGeneratedKey(form);
        transcribeGroupService.getRepository().insertOnGeneratedKey(form);
        return ResponseEntity.created(URI.create("api/v1/admin/stt/transcribe/group")).body(create());
    }

    @GetMapping("transcribe-group")
    public ResponseEntity<JsonResult<List<TranscribeGroupResponse>>> transcribeGroup() {
        final List<TranscribeGroupResponse> list = transcribeGroupService.getRepository().findAll().stream()
                .map((e) -> convertDto(e, TranscribeGroupResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(list));
    }

    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody TranscribeGroupFormRequest form, BindingResult bindingResult,
                                                @PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        transcribeGroupService.getRepository().update(form, seq);
        return ResponseEntity.ok(create());
    }

    @PutMapping(value = "status/{seq}")
    public ResponseEntity<JsonResult<Void>> statusUpdate(@PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        transcribeGroupService.getRepository().statusUpdate(seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        transcribeGroupService.getRepository().delete(seq);
        return ResponseEntity.ok(create());
    }

    @PostMapping(value = "upload/{seq}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> upload(@Valid TranscribeFileFormRequest form, BindingResult bindingResult,
                                                   @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        //transcribeGroupService.getRepository().uploadFile(form, seq);
        transcribeGroupService.uploadFile(form, seq);

        return ResponseEntity.ok(create());
    }

    @PostMapping("{fileSeq}/execute")
    public ResponseEntity<JsonResult<Integer>> execute(@PathVariable Integer fileSeq) throws IOException {
        int result;
        try {
            Runtime.getRuntime().exec("/home/ippbxmng/lib/upload_transcribe.sh " + fileSeq + " " + g.getUser().getCompanyId());

            result = 1;
        } catch (Exception e) {
            result = 0;
        }

        return ResponseEntity.ok(JsonResult.data(result));
    }

/*
    @PostMapping(value = "{seq}/fields/by-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> postFieldsByExcel(@PathVariable Integer seq, @RequestParam MultipartFile file) throws IOException {
        commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.PRV, seq, file);
        return ResponseEntity.ok(create());
    }
*/
}
