package kr.co.eicn.ippbx.front.service.api.wtalk;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.enums.TalkChannelType;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class WtalkTemplateApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/admin/wtalk/template/";

    @SneakyThrows
    public List<WtalkTemplateSummaryResponse> list(TemplateSearchRequest search) {
        return getList(subUrl + "list", search, WtalkTemplateSummaryResponse.class).getData();
    }

    @SneakyThrows
    public WtalkTemplateSummaryResponse get(Integer seq) {
        return getData(subUrl + seq, null, WtalkTemplateSummaryResponse.class).getData();
    }

    @SneakyThrows
    public String post(TemplateForm form, String companyId) {
        if (form.getTypeMent().equals(TalkTemplateFormRequest.MentType.PHOTO) && form.isNewFile()) {
            String fileName = form.getOriginalFileName();
            val file = Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalFileName()));
            form.setFilePath(null);
            form.setOriginalFileName(null);
            Object o = sendByMultipartFile(HttpMethod.POST, subUrl, form, jsonResultType(String.class), file);
            if (StringUtils.isNotEmpty(companyId))
                uploadWebchatImageToGateway(companyId, (String) o, TalkChannelType.KAKAO);

            return (String) o;
        } else {
            return (String) sendByMultipartFile(HttpMethod.POST, subUrl, form, jsonResultType(String.class), Collections.emptyMap());
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
                uploadWebchatImageToGateway(companyId, fileName, TalkChannelType.KAKAO);
        } else {
            sendByMultipartFile(HttpMethod.PUT, subUrl + seq, form, JsonResult.class, Collections.emptyMap());
        }
    }

    @SneakyThrows
    public void delete(Integer seq) {
        delete(subUrl + seq);
    }

    public Resource getResource(String filePath) throws IOException, ResultFailException {
        return getResourceResponseAll(subUrl +"/image?filePath="+filePath);
    }

    @SneakyThrows
    public Pagination<WtalkTemplateSummaryResponse> getPagination(TemplateSearchRequest search) {
        return getPagination(subUrl, search, WtalkTemplateSummaryResponse.class).getData();
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class TemplateForm extends TalkTemplateFormRequest {
        private boolean newFile = false;
    }
}
