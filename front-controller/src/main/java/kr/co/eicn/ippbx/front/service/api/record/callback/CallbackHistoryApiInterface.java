package kr.co.eicn.ippbx.front.service.api.record.callback;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.CallbackHistoryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.form.CallbackListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackRedistFormRequest;
import kr.co.eicn.ippbx.model.search.CallbackHistorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class CallbackHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(CallbackHistoryApiInterface.class);

    private static final String subUrl = "/api/v1/admin/record/callback/history/";

    public void redistribution(CallbackRedistFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "redistribution", form);
    }

    public void put(CallbackListUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl, form);
    }

    public void delete(List<Integer> serviceSequence) throws IOException, ResultFailException {
        final JsonResult<?> result = call(subUrl, Collections.singletonMap("seq", serviceSequence), JsonResult.class, HttpMethod.DELETE, true);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    public Pagination<CallbackHistoryResponse> pagination(CallbackHistorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, CallbackHistoryResponse.class).getData();
    }

    public List<SummaryCallbackDistPersonResponse> addPersons() throws IOException, ResultFailException {
        return getList(subUrl + "add-persons", null, SummaryCallbackDistPersonResponse.class).getData();
    }
}
