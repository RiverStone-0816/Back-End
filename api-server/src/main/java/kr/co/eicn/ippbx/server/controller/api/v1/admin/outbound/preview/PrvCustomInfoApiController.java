package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.preview;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.model.entity.customdb.PrvCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PrvCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.form.PrvCustomInfoRedistributionFormRequest;
import kr.co.eicn.ippbx.model.search.PrvCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.PrvCustomInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PrvGroupRepository;
import kr.co.eicn.ippbx.server.service.PrvCustomInfoService;
import kr.co.eicn.ippbx.server.service.PrvResultCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드 관리 > 프리뷰 > 데이터관리
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/preview/custominfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class PrvCustomInfoApiController extends ApiBaseController {

    private final PrvGroupRepository groupRepository;
    private final PrvCustomInfoService service;
    private final PrvResultCustomInfoService resultService;

    @GetMapping("{groupSeq}/data")
    public ResponseEntity<JsonResult<Pagination<PrvCustomInfoEntity>>> getPagination(@PathVariable Integer groupSeq, PrvCustomInfoSearchRequest search) {
        if (!service.getRepository(groupSeq).existsTable())
            return ResponseEntity.ok(data(new Pagination<>(new ArrayList<>(), search.getPage(), 0, search.getLimit())));

        if (!resultService.getRepository(groupSeq).existsTable())
            return ResponseEntity.ok(data(new Pagination<>(new ArrayList<>(), search.getPage(), 0, search.getLimit())));

        search.setGroupSeq(groupSeq);
        return ResponseEntity.ok(data(service.getRepository(groupSeq).pagination(search)));
    }

    @GetMapping("{groupSeq}/data/{id}")
    public ResponseEntity<JsonResult<PrvCustomInfoEntity>> get(@PathVariable Integer groupSeq, @PathVariable String id) {
        return ResponseEntity.ok(data(service.getRepository(groupSeq).findOne(id)));
    }

    @PostMapping("{groupSeq}/data/redistribution")
    public ResponseEntity<JsonResult<Void>> redistribution(@PathVariable Integer groupSeq, @Valid @RequestBody PrvCustomInfoRedistributionFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository(groupSeq).redistribution(form.getCustomIdList(), form.getUserIdList());
        return ResponseEntity.ok(create());
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody PrvCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final PrvCustomInfoRepository repository = service.getRepository(form.getGroupSeq());
        repository.createTableIfNotExists();
        repository.insert(form);
        groupRepository.updateTotalCntBySeq(form.getGroupSeq(), 1, true);

        return ResponseEntity.created(URI.create("api/v1/admin/outbound/preview/custominfo")).body(create());
    }

    @PutMapping("{id}")
    public JsonResult<Void> put(@Valid @RequestBody PrvCustomInfoFormRequest form, BindingResult bindingResult,
                                @PathVariable String id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository(form.getGroupSeq()).update(form, id);
        return create();
    }

    @DeleteMapping("{groupSeq}/data/{customId}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer groupSeq, @PathVariable String customId) {
        service.getRepository(groupSeq).delete(customId);
        groupRepository.updateTotalCntBySeq(groupSeq, 1, false);
        return ResponseEntity.ok(create());
    }

    @GetMapping("preview-group")
    public ResponseEntity<JsonResult<List<PrvGroup>>> prvGroup() {
        return ResponseEntity.ok(data(groupRepository.findAll()));
    }
}
