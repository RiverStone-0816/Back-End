package kr.co.eicn.ippbx.front.service.api.talk.statistics;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.TalkStatisticsHourlyResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsHourlySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TalkHourlyStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkHourlyStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/talk/statistics/hourly/";

    public List<TalkStatisticsHourlyResponse> list(TalkStatisticsHourlySearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, TalkStatisticsHourlyResponse.class).getData();
    }
}
