package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.model.dto.eicn.HistoryPdsGroupResponse;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ExecutePdsGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.PDSResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.ExecutePDSGroupSearchRequest;
import kr.co.eicn.ippbx.model.search.PDSResultCustomInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ExecutePDSGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.HistoryPDSGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PDSGroupRepository;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.server.service.PDSResultCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/resultcustominfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSResultCustomInfoApiController extends ApiBaseController {

    private final PDSResultCustomInfoService service;
    private final HistoryPDSGroupRepository  historyPDSGroupRepository;

    @GetMapping("execute-pds-info")
    public ResponseEntity<JsonResult<List<HistoryPdsGroupResponse>>> getExecutingPdsList() {
        return ResponseEntity.ok(data(historyPDSGroupRepository.getResultExecuteList()));
    }

    @GetMapping("{executeId}/data")
    public ResponseEntity<JsonResult<Pagination<PDSResultCustomInfoEntity>>> getPagination(@PathVariable String executeId, PDSResultCustomInfoSearchRequest search) {
        search.setExecuteId(executeId);
        return ResponseEntity.ok(data(service.getRepository(executeId).pagination(search)));
    }

    @GetMapping("{executeId}/data/{seq}")
    public ResponseEntity<JsonResult<PDSResultCustomInfoEntity>> get(@PathVariable String executeId, @PathVariable Integer seq) {
        return ResponseEntity.ok(data(service.getRepository(executeId).findOne(seq)));
    }

    @PutMapping("{executeId}/data/{seq}")
    public JsonResult<Void> put(@Valid @RequestBody PDSResultCustomInfoFormRequest form, BindingResult bindingResult,
            @PathVariable String executeId, @PathVariable Integer seq
    ) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        PDSResultCustomInfoEntity response = service.getRepository(executeId).findOne(seq);
        if (g.getUser().getIdType().equals(IdType.USER.getCode()) && !g.getUser().getId().equals(response.getUserid()))
            throw new IllegalArgumentException("다른 상담원의 이력은 수정할 수 없습니다.");

        service.getRepository(executeId).update(form, seq);
        return create();
    }

    @DeleteMapping("{executeId}/data/{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String executeId, @PathVariable Integer seq) {

        PDSResultCustomInfoEntity response = service.getRepository(executeId).findOne(seq);
        if (g.getUser().getIdType().equals(IdType.USER.getCode()) && !g.getUser().getId().equals(response.getUserid()))
            throw new IllegalArgumentException("다른 상담원의 이력은 삭제할 수 없습니다.");

        service.getRepository(executeId).deleteData(seq);
        return ResponseEntity.ok(create());
    }
}
