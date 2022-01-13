package kr.co.eicn.ippbx.front.service.api.talk;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
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

    @SneakyThrows
    public Integer post(TemplateForm form, String companyId) {
        if (form.getTypeMent().equals(TalkTemplateFormRequest.MentType.PHOTO) && form.isNewFile()) {
            String fileName = form.getOriginalFileName();
            val file = Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalFileName()));
            form.setFilePath(null);
            form.setOriginalFileName(null);
            Object o = sendByMultipartFile(HttpMethod.POST, subUrl, form, jsonResultType(Integer.class), file);
            if (StringUtils.isNotEmpty(companyId))
                uploadWebchatImageToGateway(companyId, fileName);

            return (Integer) o;
        } else {
            return (Integer) sendByMultipartFile(HttpMethod.POST, subUrl, form, jsonResultType(Integer.class), Collections.emptyMap());
        }
    }

    @SneakyThrows
    public void put(Integer seq, TemplateForm form, String companyId) {
        if (form.getTypeMent().equals(TalkTemplateFormRequest.MentType.PHOTO) && form.isNewFile()) {
            String fileName = form.getOriginalFileName();
            val file = Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalFileName()));
            form.setFilePath(null);
            form.setOriginalFileName(null);

            sendByMultipartFile(HttpMethod.PUT, subUrl + seq, form, JsonResult.class, file);

            if (StringUtils.isNotEmpty(companyId))
                uploadWebchatImageToGateway(companyId, fileName);
        } else {
            sendByMultipartFile(HttpMethod.PUT, subUrl + seq, form, JsonResult.class, Collections.emptyMap());
        }
    }

    @SneakyThrows
    public void delete(Integer seq) {
        delete(subUrl + seq);
    }

    @SneakyThrows
    public Pagination<TalkTemplateSummaryResponse> getPagination(TemplateSearchRequest search) {
        return getPagination(subUrl, search, TalkTemplateSummaryResponse.class).getData();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class TemplateForm extends TalkTemplateFormRequest {
        private boolean newFile = false;
    }
}
