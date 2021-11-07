package kr.co.eicn.ippbx.front.controller.api.record.callback;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackDistributionApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.CallbackDistListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryQueueResponse;
import kr.co.eicn.ippbx.model.form.CallbackHuntDistFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackUserDistFormRequest;
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
@RequestMapping(value = "api/callback-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
public class CallbackDistributionApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CallbackDistributionApiController.class);

    @Autowired
    private CallbackDistributionApiInterface apiInterface;

    @PostMapping("service/{svcNumber}/hunts")
    public void huntDistribution(@RequestBody @Valid CallbackHuntDistFormRequest form, BindingResult bindingResult, @PathVariable String svcNumber) throws IOException, ResultFailException {
        apiInterface.huntDistribution(svcNumber, form);
    }

    @PostMapping("service/{svcNumber}/hunt/{huntNumber}/users")
    public void userDistribution(@RequestBody @Valid CallbackUserDistFormRequest form, BindingResult bindingResult, @PathVariable String svcNumber, @PathVariable String huntNumber) throws IOException, ResultFailException {
        apiInterface.userDistribution(svcNumber, huntNumber, form);
    }

    @GetMapping("")
    public List<CallbackDistListResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    /**
     * 분배 가능한 큐 목록조회
     */
    @GetMapping("add-hunts")
    public List<SummaryQueueResponse> addHunts(@RequestParam(required = false) String svcNumber) throws IOException, ResultFailException {
        return apiInterface.addHunts(svcNumber);
    }

    /**
     * 분배 가능한 사용자 목록조회
     */
    @GetMapping("add-persons")
    public List<SummaryCallbackDistPersonResponse> addPersons() throws IOException, ResultFailException {
        return apiInterface.addPersons();
    }
}
