package kr.co.eicn.ippbx.front.controller.api.application.code;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.code.EnumerationValueApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonFieldResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonTypeDetailResponse;
import kr.co.eicn.ippbx.server.model.form.CommonCodeUpdateFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/enumeration-value", produces = MediaType.APPLICATION_JSON_VALUE)
public class EnumerationValueApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(EnumerationValueApiController.class);

    @Autowired
    private EnumerationValueApiInterface apiInterface;

    @GetMapping("")
    public List<CommonTypeDetailResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("type/{type}/field/{fieldId}")
    public CommonFieldResponse getField(@PathVariable String fieldId, @PathVariable Integer type) throws IOException, ResultFailException {
        return apiInterface.getField(type, fieldId);
    }

    @PutMapping("type/{type}/field/{fieldId}/code")
    public void put(@Valid @RequestBody CommonCodeUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer type, @PathVariable String fieldId) throws IOException, ResultFailException {
        apiInterface.put(type, fieldId, form);
    }

    @PostMapping("{type}/{fieldId}/codes/by-excel")
    public void postFieldsByExcel(@RequestBody FileForm form, @PathVariable Integer type, @PathVariable String fieldId) throws IOException, ResultFailException {
        apiInterface.postFieldsByExcel(type, fieldId, form);
    }
}
