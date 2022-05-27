package kr.co.eicn.ippbx.front.service.api.wtalk.statistics;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsHourlyResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsHourlySearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WtalkHourlyStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(WtalkHourlyStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/wtalk/statistics/hourly/";

    public List<WtalkStatisticsHourlyResponse> list(TalkStatisticsHourlySearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, WtalkStatisticsHourlyResponse.class).getData();
    }
}
