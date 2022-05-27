package kr.co.eicn.ippbx.front.service.api.wtalk.group;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMentDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMentSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMentFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Service
public class WtalkGroupAutoCommentApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkGroupAutoCommentApiInterface.class);

    private static final String subUrl = "/api/v1/admin/wtalk/group/auto-comment/";

    public List<WtalkMentSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, WtalkMentSummaryResponse.class).getData();
    }

    public WtalkMentDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, WtalkMentDetailResponse.class).getData();
    }

    public Integer post(TalkMentFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, TalkMentFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
