package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.preview;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.entity.customdb.PrvResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PrvResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.PrvResultCustomInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PrvGroupRepository;
import kr.co.eicn.ippbx.server.service.PrvCustomInfoService;
import kr.co.eicn.ippbx.server.service.PrvResultCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드 관리 > 프리뷰 > 결과이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/preview/resultcustominfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrvResultCustomInfoApiController extends ApiBaseController {

    private final PrvGroupRepository groupRepository;
    private final PrvResultCustomInfoService service;
    private final PrvCustomInfoService infoService;

    @GetMapping("{groupSeq}/data")
    public ResponseEntity<JsonResult<Pagination<PrvResultCustomInfoEntity>>> getPagination(@PathVariable Integer groupSeq, PrvResultCustomInfoSearchRequest search) {
        final Pagination<PrvResultCustomInfoEntity> pagination = service.getRepository(groupSeq).pagination(search);
        if (!service.getRepository(groupSeq).existsTable())
            return ResponseEntity.ok(data(new Pagination<>(new ArrayList<>(), search.getPage(), 0, search.getLimit())));

        if (!infoService.getRepository(groupSeq).existsTable())
            return ResponseEntity.ok(data(new Pagination<>(new ArrayList<>(), search.getPage(), 0, search.getLimit())));

        search.setGroupSeq(groupSeq);
        final List<PrvResultCustomInfoEntity> rows = pagination.getRows().stream().filter(e -> !e.getGroupKind().contains("TMP"))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{groupSeq}/data/{seq}")
    public ResponseEntity<JsonResult<PrvResultCustomInfoEntity>> get(@PathVariable Integer groupSeq, @PathVariable Integer seq) {
        final PrvResultCustomInfoRepository repository = service.getRepository(groupSeq);
        if (!repository.existsTable())
            throw new IllegalStateException("테이블이 존재하지 않습니다: " + repository.getTable().getName());

        return ResponseEntity.ok(data(repository.findOne(seq)));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final PrvResultCustomInfoRepository repository = service.getRepository(form.getGroupId());
        repository.createTableIfNotExists();

        if (StringUtils.isNotEmpty(form.getClickKey()) && !form.getClickKey().equals("nonClickKey") && !form.getGroupKind().equals("TALK")) {
            final PrvResultCustomInfoSearchRequest search = new PrvResultCustomInfoSearchRequest();
            search.setClickKey(form.getClickKey());
            final List<PrvResultCustomInfoEntity> seqCheck = repository.getOne(search);

            if (!CollectionUtils.isEmpty(seqCheck))
                repository.update(form, seqCheck.get(0).getSeq());
            else
                repository.insert(form);
        } else
            repository.insert(form);

        return ResponseEntity.created(URI.create("api/v1/admin/outbound/preview/custominfo_add")).body(create());
    }

    @PutMapping("{seq}")
    public JsonResult<Void> put(@Valid @RequestBody ResultCustomInfoFormRequest form, BindingResult bindingResult,
                                @PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final PrvResultCustomInfoRepository repository = service.getRepository(form.getGroupId());
        if (!repository.existsTable())
            throw new IllegalStateException("테이블이 존재하지 않습니다: " + repository.getTable().getName());

        PrvResultCustomInfoEntity response = repository.findOne(seq);
        if (g.getUser().getIdType().equals(IdType.USER.getCode()) && !g.getUser().getId().equals(response.getUserid()))
            throw new IllegalArgumentException("다른 상담원의 이력은 수정할 수 없습니다.");

        repository.update(form, seq);
        return create();
    }

    @DeleteMapping("{groupSeq}/data/{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer groupSeq, @PathVariable Integer seq) {
        final PrvResultCustomInfoRepository repository = service.getRepository(groupSeq);
        if (!repository.existsTable())
            throw new IllegalStateException("테이블이 존재하지 않습니다: " + repository.getTable().getName());

        PrvResultCustomInfoEntity response = repository.findOne(seq);
        if (g.getUser().getIdType().equals(IdType.USER.getCode()) && !g.getUser().getId().equals(response.getUserid()))
            throw new IllegalArgumentException("다른 상담원의 이력은 삭제할 수 없습니다.");

        repository.delete(seq);
        return ResponseEntity.ok(create());
    }
}
