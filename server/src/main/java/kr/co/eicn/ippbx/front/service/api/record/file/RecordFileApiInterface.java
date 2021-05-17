package kr.co.eicn.ippbx.front.service.api.record.file;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.DiskResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.FileSummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class RecordFileApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileApiInterface.class);

    private static final String subUrl = "/api/v1/admin/record/file/";

    public List<FileSummaryResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, FileSummaryResponse.class).getData();
    }

    public void delete(String fileName) throws IOException, ResultFailException {
        super.delete(subUrl + URLEncoder.encode(fileName));
    }

    public DiskResponse disk() throws IOException, ResultFailException {
        return getData(subUrl + "disk", null, DiskResponse.class).getData();
    }
}
