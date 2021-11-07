package kr.co.eicn.ippbx.front.service.api.application.sms;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendMessageHistoryResponse;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SmsMessageHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageHistoryApiInterface.class);

    private static final String subUrl = "/api/v1/admin/application/sms/message-history/";

    public Pagination<SendMessageHistoryResponse> pagination(SendMessageSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, SendMessageHistoryResponse.class).getData();
    }
}
