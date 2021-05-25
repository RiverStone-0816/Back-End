package kr.co.eicn.ippbx.front.service.api.application.mail;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendFaxEmailHistoryResponse;
import kr.co.eicn.ippbx.model.search.SendFaxEmailHistorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MailHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MailHistoryApiInterface.class);

    private static final String subUrl = "/api/v1/admin/application/fax-email/history/";

    public Pagination<SendFaxEmailHistoryResponse> pagination(SendFaxEmailHistorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, SendFaxEmailHistoryResponse.class).getData();
    }
}
