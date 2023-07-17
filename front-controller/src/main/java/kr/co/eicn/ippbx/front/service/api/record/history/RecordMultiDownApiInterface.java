package kr.co.eicn.ippbx.front.service.api.record.history;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.RecordDownSummaryResponse;
import kr.co.eicn.ippbx.model.search.RecordDownSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RecordMultiDownApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(RecordMultiDownApiInterface.class);

    private static final String subUrl = "/api/v1/admin/record/history/record-multi-down/";

    public List<RecordDownSummaryResponse> list(RecordDownSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, RecordDownSummaryResponse.class).getData();
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public Resource getResource(Integer id) throws IOException, ResultFailException {
        return getResourceResponseAll(subUrl + id+"/resource");
    }
}
