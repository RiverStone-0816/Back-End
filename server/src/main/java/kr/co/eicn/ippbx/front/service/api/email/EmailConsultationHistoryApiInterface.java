package kr.co.eicn.ippbx.front.service.api.email;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonCodeResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.EmailConsultationHistorySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.EmailConsultationHistoryFormRequest;
import kr.co.eicn.ippbx.server.model.search.EmailConsultationHistorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailConsultationHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EmailConsultationHistoryApiInterface.class);

    private static final String subUrl = "/api/v1/admin/email/history/consultation-history/";

    public Pagination<EmailConsultationHistorySummaryResponse> pagination(EmailConsultationHistorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, EmailConsultationHistorySummaryResponse.class).getData();
    }

    public void redistribution(EmailConsultationHistoryFormRequest form) throws IOException, ResultFailException {
        post(subUrl + "redistribution", form);
    }

    public List<CommonCodeResponse> emailCommonCode() throws IOException, ResultFailException {
        return getList(subUrl + "common-code", null, CommonCodeResponse.class).getData();
    }
}
