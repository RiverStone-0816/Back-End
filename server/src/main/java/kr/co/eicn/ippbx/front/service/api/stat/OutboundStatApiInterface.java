package kr.co.eicn.ippbx.front.service.api.stat;

import com.fasterxml.jackson.databind.JavaType;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.JsonResult;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatOutboundResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatOutboundTimeResponse;
import kr.co.eicn.ippbx.server.model.dto.util.*;
import kr.co.eicn.ippbx.server.model.enums.SearchCycle;
import kr.co.eicn.ippbx.server.model.search.StatOutboundSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class OutboundStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(OutboundStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stat/outbound/";

    @SuppressWarnings("unchecked")
    public List<StatOutboundTimeResponse<?>> list(StatOutboundSearchRequest search) throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(List.class,
                typeFactory.constructParametricType(StatOutboundTimeResponse.class,
                        SearchCycle.DATE.equals(search.getTimeUnit()) ? DateResponse.class
                                : SearchCycle.HOUR.equals(search.getTimeUnit()) ? HourResponse.class
                                : SearchCycle.WEEK.equals(search.getTimeUnit()) ? WeekResponse.class
                                : SearchCycle.MONTH.equals(search.getTimeUnit()) ? MonthResponse.class
                                : SearchCycle.DAY_OF_WEEK.equals(search.getTimeUnit()) ? DayOfWeekResponse.class
                                : DateResponse.class
                )
        );

        return (List<StatOutboundTimeResponse<?>>) getData(subUrl, search, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }

    public StatOutboundResponse total(StatOutboundSearchRequest search)  throws IOException, ResultFailException {
        return getData(subUrl + "total", search, StatOutboundResponse.class).getData();
    }
}
