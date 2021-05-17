package kr.co.eicn.ippbx.front.service.api.application.maindb;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.GroupUploadLogDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.GroupUploadLogResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.MaindbUploadResponse;
import kr.co.eicn.ippbx.server.model.search.MaindbUploadSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MaindbUploadApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MaindbUploadApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/maindb/upload/";

    public Pagination<MaindbUploadResponse> pagination(MaindbUploadSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, MaindbUploadResponse.class).getData();
    }

    public GroupUploadLogDetailResponse uploadLog(String uploadId) throws IOException, ResultFailException {
        return getData(subUrl + "upload-log/" + uploadId, null, GroupUploadLogDetailResponse.class).getData();
    }

    public List<GroupUploadLogResponse> maindbGroup() throws IOException, ResultFailException {
        return getList(subUrl + "maindb-group", null, GroupUploadLogResponse.class).getData();
    }
}
