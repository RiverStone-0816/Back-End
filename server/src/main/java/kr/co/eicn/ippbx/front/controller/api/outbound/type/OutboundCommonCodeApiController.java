package kr.co.eicn.ippbx.front.controller.api.outbound.type;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.type.OutboundCommonCodeApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonTypeDetailResponse;
import kr.co.eicn.ippbx.server.model.form.CommonCodeUpdateFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/outbound-code", produces = MediaType.APPLICATION_JSON_VALUE)
public class OutboundCommonCodeApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundCommonCodeApiController.class);

    @Autowired
    private OutboundCommonCodeApiInterface apiInterface;

    @GetMapping(value = "", params = "kind")
    public List<CommonTypeDetailResponse> list(@RequestParam String kind) throws IOException, ResultFailException {
        return apiInterface.list(kind);
    }

    @PutMapping("type/{type}/field/{fieldId}/code")
    public void put(@RequestBody CommonCodeUpdateFormRequest form, @PathVariable Integer type, @PathVariable String fieldId) throws IOException, ResultFailException {
        apiInterface.put(type, fieldId, form);
    }

    @PostMapping("{type}/{fieldId}/codes/by-excel")
    public void postFieldsByExcel(@RequestBody FileForm form, @PathVariable Integer type, @PathVariable String fieldId) throws IOException, ResultFailException {
        apiInterface.postFieldsByExcel(type, fieldId, form);
    }
}
