package kr.co.eicn.ippbx.front.service.api.talk.statistics;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.TalkStatisticsPersonResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TalkPersonStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TalkPersonStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/talk/statistics/person/";

    public List<TalkStatisticsPersonResponse> list(TalkStatisticsSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, TalkStatisticsPersonResponse.class).getData();
    }
}
