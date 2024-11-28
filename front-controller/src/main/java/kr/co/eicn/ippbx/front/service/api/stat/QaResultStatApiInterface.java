package kr.co.eicn.ippbx.front.service.api.stat;

import kr.co.eicn.ippbx.model.dto.eicn.FieldCodeResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultIndividualResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultResponse;
import kr.co.eicn.ippbx.model.search.StatQaResultSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class QaResultStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(QaResultStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stat/result/";

    public List<StatQaResultResponse> list(StatQaResultSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, StatQaResultResponse.class).getData();
    }

    public List<StatQaResultIndividualResponse> getIndividualResult(StatQaResultSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "individual", search, StatQaResultIndividualResponse.class).getData();
    }

    public List<FieldCodeResponse> getIndividualFieldList() throws IOException, ResultFailException {
        return getList(subUrl + "individual/field-list", null, FieldCodeResponse.class).getData();
    }
}
