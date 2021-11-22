package kr.co.eicn.ippbx.front.service.api.talk;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TalkTemplateApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/admin/talk/template/";

    @SneakyThrows
    public List<TalkTemplateSummaryResponse> list(TemplateSearchRequest search) {
        return getList(subUrl + "list", search, TalkTemplateSummaryResponse.class).getData();
    }

    @SneakyThrows
    public TalkTemplateSummaryResponse get(Integer seq) {
        return getData(subUrl + seq, null, TalkTemplateSummaryResponse.class).getData();
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public Integer post(TalkTemplateFormRequest form) {
        val response = (JsonResult<Integer>) sendByMultipartFile(HttpMethod.POST, subUrl, form, jsonResultType(Integer.class),
                form.getTypeMent().equals(TalkTemplateFormRequest.MentType.PHOTO) ? Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalFileName())) : Collections.emptyMap());
        return response.getData();
    }

    @SneakyThrows
    public void put(Integer seq, TalkTemplateFormRequest form) {
        sendByMultipartFile(HttpMethod.PUT, subUrl + seq, form, JsonResult.class,
                form.getTypeMent().equals(TalkTemplateFormRequest.MentType.PHOTO) ? Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalFileName())) : Collections.emptyMap());
    }

    @SneakyThrows
    public void delete(Integer seq) {
        delete(subUrl + seq);
    }

    @SneakyThrows
    public Pagination<TalkTemplateSummaryResponse> getPagination(TemplateSearchRequest search) {
        return getPagination(subUrl, search, TalkTemplateSummaryResponse.class).getData();
    }

}
