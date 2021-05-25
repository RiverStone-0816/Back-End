package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.GroupUploadLogResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PDSUploadSummaryResponse;
import kr.co.eicn.ippbx.model.search.PDSUploadSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdsUploadApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsUploadApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/upload/";

    public Pagination<PDSUploadSummaryResponse> pagination(PDSUploadSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PDSUploadSummaryResponse.class).getData();
    }

    public GroupUploadLogDetailResponse uploadLog(String uploadId) throws IOException, ResultFailException {
        return getData(subUrl + "upload-log/" + uploadId, null, GroupUploadLogDetailResponse.class).getData();
    }

    public List<GroupUploadLogResponse> pdsGroup() throws IOException, ResultFailException {
        return getList(subUrl + "pds-group", null, GroupUploadLogResponse.class).getData();
    }
}
