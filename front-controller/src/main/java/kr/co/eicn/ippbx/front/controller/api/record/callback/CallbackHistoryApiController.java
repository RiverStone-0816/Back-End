package kr.co.eicn.ippbx.front.controller.api.record.callback;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.CallbackHistoryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.form.CallbackListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackRedistFormRequest;
import kr.co.eicn.ippbx.model.search.CallbackHistorySearchRequest;
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
@RequestMapping(value = "api/callback-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class CallbackHistoryApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CallbackHistoryApiController.class);

    @Autowired
    private CallbackHistoryApiInterface apiInterface;

    @PutMapping("/redistribution")
    public void redistribution(@RequestBody @Valid CallbackRedistFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.redistribution(form);
    }

    @PutMapping("")
    public void put(@Valid @RequestBody CallbackListUpdateFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.put(form);
    }

    @DeleteMapping("")
    public void delete(@RequestParam List<Integer> serviceSequence) throws IOException, ResultFailException {
        apiInterface.delete(serviceSequence);
    }

    @GetMapping("")
    public Pagination<CallbackHistoryResponse> pagination(CallbackHistorySearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    /**
     * 분배 가능한 사용자 목록조회
     */
    @GetMapping("add-persons")
    public List<SummaryCallbackDistPersonResponse> addPersons() throws IOException, ResultFailException {
        return apiInterface.addPersons();
    }
}
