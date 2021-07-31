package kr.co.eicn.ippbx.front.service.api.talk;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TalkTemplateApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkTemplateApiInterface.class);

    private static final String subUrl = "/api/v1/admin/talk/template/";

    public List<TalkTemplateSummaryResponse> list(TemplateSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, TalkTemplateSummaryResponse.class).getData();
    }

    public TalkTemplateSummaryResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, TalkTemplateSummaryResponse.class).getData();
    }

    public Integer post(TalkTemplateFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, TalkTemplateFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
