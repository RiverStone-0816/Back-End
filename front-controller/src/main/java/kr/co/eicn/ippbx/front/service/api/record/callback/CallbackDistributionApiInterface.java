package kr.co.eicn.ippbx.front.service.api.record.callback;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.CallbackDistListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryQueueResponse;
import kr.co.eicn.ippbx.model.form.CallbackHuntDistFormRequest;
import kr.co.eicn.ippbx.model.form.CallbackUserDistFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CallbackDistributionApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(CallbackDistributionApiInterface.class);

    private static final String subUrl = "/api/v1/admin/record/callback/distribution/";

    public void huntDistribution(String svcNumber, CallbackHuntDistFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "service/" + svcNumber + "/hunts", form);
    }

    public void userDistribution(String svcNumber, String huntNumber, CallbackUserDistFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "service/" + svcNumber + "/hunt/" + huntNumber + "/users", form);
    }

    public List<CallbackDistListResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, CallbackDistListResponse.class).getData();
    }

    public List<SummaryQueueResponse> addHunts() throws IOException, ResultFailException {
        return addHunts("");
    }

    public List<SummaryQueueResponse> addHunts(String svcNumber) throws IOException, ResultFailException {
        return getList(subUrl + "divisible-hunts?svcNumber=" + svcNumber, null, SummaryQueueResponse.class).getData();
    }

    public List<SummaryCallbackDistPersonResponse> addPersons() throws IOException, ResultFailException {
        return getList(subUrl + "divisible-persons", null, SummaryCallbackDistPersonResponse.class).getData();
    }
}
