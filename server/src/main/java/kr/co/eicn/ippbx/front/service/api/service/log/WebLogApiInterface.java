package kr.co.eicn.ippbx.front.service.api.service.log;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.JsonResult;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.LoginInfoResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.WebSecureHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.WebSecureHistorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class WebLogApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WebLogApiInterface.class);

    private static final String subUrl = "/api/v1/admin/service/log/web-log/";

    public Pagination<WebSecureHistoryResponse> pagination(WebSecureHistorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, WebSecureHistoryResponse.class).getData();
    }

    public void delete(List<Integer> checkIds) throws IOException, ResultFailException {
        final JsonResult<?> result = call(subUrl, Collections.singletonMap("seq", checkIds), JsonResult.class, HttpMethod.DELETE, true);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    public LoginInfoResponse getLastLoginInfo(String userId) throws IOException, ResultFailException {
        return getData(subUrl + userId + "/last-login", null, LoginInfoResponse.class).getData();
    }

    public void overwrite(Integer limit) throws IOException, ResultFailException {
        super.delete(subUrl + "overwrite/" + limit);
    }

    public void updateWebLogDown(String type) throws IOException, ResultFailException {
        put(subUrl + "down/" + type, null);
    }
}
