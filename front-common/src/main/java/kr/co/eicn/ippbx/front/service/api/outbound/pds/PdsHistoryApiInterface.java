package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.PDSHistoryResponse;
import kr.co.eicn.ippbx.model.search.PDSHistorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdsHistoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsHistoryApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/history/";

    public Pagination<PDSHistoryResponse> pagination(PDSHistorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PDSHistoryResponse.class).getData();
    }

    public void delete(String executeId) throws IOException, ResultFailException {
        super.delete(subUrl + "execution/" + executeId);
    }
}
