package kr.co.eicn.ippbx.front.service.api.application.mail;

import kr.co.eicn.ippbx.front.model.form.MailFileForm;
import kr.co.eicn.ippbx.front.model.form.MailFileUpdateForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendFileResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class MailFileApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MailFileApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/fax-email/file/";

    public Pagination<SendFileResponse> pagination(SendCategorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, SendFileResponse.class).getData();
    }

    public SendFileResponse get(Long id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, SendFileResponse.class).getData();
    }

    public Long post(MailFileForm form) throws IOException, ResultFailException {
        return sendByMultipartFile(HttpMethod.POST, subUrl, form, Long.class, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }

    public void put(Long id, MailFileUpdateForm form) throws IOException, ResultFailException {
        sendByMultipartFile(HttpMethod.POST, subUrl + id, form, Long.class, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }

    public void delete(Long id) throws IOException, ResultFailException {
        delete(subUrl + id);
    }

    public List<SendSmsCategorySummaryResponse> sendCategory() throws IOException, ResultFailException {
        return getList(subUrl + "category", null, SendSmsCategorySummaryResponse.class).getData();
    }
}
