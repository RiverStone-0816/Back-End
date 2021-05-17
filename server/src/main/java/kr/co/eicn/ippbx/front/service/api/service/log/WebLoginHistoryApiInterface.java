package kr.co.eicn.ippbx.front.service.api.service.log;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.LoginHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.LoginHistorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebLoginHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WebLoginHistoryApiInterface.class);

    private static final String subUrl = "/api/v1/admin/service/log/history/";

    public Pagination<LoginHistoryResponse> pagination(LoginHistorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, LoginHistoryResponse.class).getData();
    }
}
