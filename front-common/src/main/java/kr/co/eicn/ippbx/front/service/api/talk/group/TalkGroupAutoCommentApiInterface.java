package kr.co.eicn.ippbx.front.service.api.talk.group;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMentDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMentSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMentFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Service
public class TalkGroupAutoCommentApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkGroupAutoCommentApiInterface.class);

    private static final String subUrl = "/api/v1/admin/talk/group/auto-comment/";

    public List<TalkMentSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, TalkMentSummaryResponse.class).getData();
    }

    public TalkMentDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, TalkMentDetailResponse.class).getData();
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
