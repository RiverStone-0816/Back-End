package kr.co.eicn.ippbx.front.service.api.wtalk.info;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WtalkServiceApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkServiceApiInterface.class);

    private static final String subUrl = "/api/v1/admin/wtalk/info/service/";

    public List<WtalkServiceSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, WtalkServiceSummaryResponse.class).getData();
    }

    public WtalkServiceDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, WtalkServiceDetailResponse.class).getData();
    }

    public Integer post(TalkServiceInfoFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, TalkServiceInfoFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
