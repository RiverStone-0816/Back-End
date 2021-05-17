package kr.co.eicn.ippbx.front.service.api.application.sms;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendMessageTemplateResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.SendCategoryUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.SendMessageTemplateFormRequest;
import kr.co.eicn.ippbx.server.model.form.SendMessageTemplateUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.SendSmsCategoryFormRequest;
import kr.co.eicn.ippbx.server.model.search.SendMessageTemplateSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SmsMessageTemplateApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageTemplateApiInterface.class);

    private static final String subUrl = "/api/v1/admin/application/sms/message-template/";

    public Pagination<SendMessageTemplateResponse> pagination(SendMessageTemplateSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, SendMessageTemplateResponse.class).getData();
    }

    public SendMessageTemplateResponse get(Integer id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, SendMessageTemplateResponse.class).getData();
    }

    public Long post(SendMessageTemplateFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Long.class, false).getData();
    }

    public void put(Integer id, SendMessageTemplateUpdateRequest form) throws IOException, ResultFailException {
        super.put(subUrl + id, form);
    }

    public void delete(Integer id) throws IOException, ResultFailException {
        delete(subUrl + id);
    }

    public List<SendSmsCategorySummaryResponse> sendCategory() throws IOException, ResultFailException {
        return getList(subUrl + "category", null, SendSmsCategorySummaryResponse.class).getData();
    }
}
