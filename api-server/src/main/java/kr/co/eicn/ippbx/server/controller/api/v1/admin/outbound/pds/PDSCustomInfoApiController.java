package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;


import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.entity.pds.PDSCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.form.PDSCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.model.search.PDSDataSearchRequest;
import kr.co.eicn.ippbx.server.service.PDSCustomInfoService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/custominfo", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSCustomInfoApiController extends ApiBaseController {

    private final PDSCustomInfoService service;

    @GetMapping("{groupSeq}/data")
    public ResponseEntity<JsonResult<Pagination<PDSCustomInfoEntity>>> getPagination(@PathVariable Integer groupSeq, PDSDataSearchRequest search) {
        search.setGroupSeq(groupSeq);
        return ResponseEntity.ok(data(service.getRepository(groupSeq).pagination(search)));
    }

    @GetMapping("{groupSeq}/data/{id}")
    public ResponseEntity<JsonResult<PDSCustomInfoEntity>> get(@PathVariable Integer groupSeq, @PathVariable String id) {
        return ResponseEntity.ok(data(service.getRepository(groupSeq).findOne(groupSeq, id)));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody PDSCustomInfoFormRequest form, BindingResult bindingResult) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository(form.getGroupSeq()).insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/outbound/pds/custominfo_add")).body(create());

    }

    @PutMapping("{id}")
    public JsonResult<Void> put(@Valid @RequestBody PDSCustomInfoFormRequest form, BindingResult bindingResult,
                                @PathVariable String id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository(form.getGroupSeq()).update(form, id);
        return create();
    }

    @DeleteMapping("")
    public ResponseEntity<JsonResult<Void>> deleteData(@RequestParam Integer groupSeq, @RequestParam String id) {
        service.getRepository(groupSeq).deleteData(id);
        return ResponseEntity.ok(create());
    }
}
