package kr.co.eicn.ippbx.front.controller.api.application.code;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.code.EnumerationValueMappingApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ConCodeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConGroupResponse;
import kr.co.eicn.ippbx.model.form.ConCodeFormRequest;
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
@RequestMapping(value = "api/enumeration-value-mapping", produces = MediaType.APPLICATION_JSON_VALUE)
public class EnumerationValueMappingApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EnumerationValueMappingApiController.class);

    @Autowired
    private EnumerationValueMappingApiInterface apiInterface;

    @GetMapping("")
    public List<ConCodeResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @PutMapping("type/{type}/field/{fieldId}/code")
    public void put(@Valid @RequestBody ConCodeFormRequest form, BindingResult bindingResult, @PathVariable Integer type, @PathVariable String fieldId) throws IOException, ResultFailException {
        apiInterface.put(type, fieldId, form);
    }

    @GetMapping("group")
    public List<ConGroupResponse> getConGroupList() throws IOException, ResultFailException {
        return apiInterface.getConGroupList();
    }
}
