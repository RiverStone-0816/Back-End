package kr.co.eicn.ippbx.front.service.api.outbound.voc;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.StatVocResponse;
import kr.co.eicn.ippbx.model.search.StatDBVOCStatisticsSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class VocStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(VocStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/voc/statistics/";

    public List<StatVocResponse> list(StatDBVOCStatisticsSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, StatVocResponse.class).getData();
    }
}
