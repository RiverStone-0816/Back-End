package kr.co.eicn.ippbx.front.service.api.wtalk.statistics;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsPersonResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WtalkPersonStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkPersonStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/wtalk/statistics/person/";

    public List<WtalkStatisticsPersonResponse> list(TalkStatisticsSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, WtalkStatisticsPersonResponse.class).getData();
    }
}
