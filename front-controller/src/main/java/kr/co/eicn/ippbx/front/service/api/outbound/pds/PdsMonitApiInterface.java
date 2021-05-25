package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.PDSExecuteListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PDSMonitResponse;
import kr.co.eicn.ippbx.model.search.PDSMonitSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdsMonitApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsMonitApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/monit/";

    public Pagination<PDSMonitResponse> pagination(PDSMonitSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PDSMonitResponse.class).getData();
    }

    public List<PDSExecuteListResponse> getPDSList(PDSMonitSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "running-pds-list", search, PDSExecuteListResponse.class).getData();
    }
}
