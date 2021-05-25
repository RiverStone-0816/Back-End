package kr.co.eicn.ippbx.front.service.api.stat;

import com.fasterxml.jackson.databind.JavaType;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.StatHuntInboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatHuntResponse;
import kr.co.eicn.ippbx.model.dto.util.*;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatHuntSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class HuntStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(HuntStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stat/hunt/";

    @SuppressWarnings("unchecked")
    public List<StatHuntResponse<?>> list(StatHuntSearchRequest search) throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(List.class,
                typeFactory.constructParametricType(StatHuntResponse.class,
                        SearchCycle.DATE.equals(search.getTimeUnit()) ? DateResponse.class
                                : SearchCycle.HOUR.equals(search.getTimeUnit()) ? HourResponse.class
                                : SearchCycle.WEEK.equals(search.getTimeUnit()) ? WeekResponse.class
                                : SearchCycle.MONTH.equals(search.getTimeUnit()) ? MonthResponse.class
                                : SearchCycle.DAY_OF_WEEK.equals(search.getTimeUnit()) ? DayOfWeekResponse.class
                                : DateResponse.class
                )
        );

        return (List<StatHuntResponse<?>>) getData(subUrl, search, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }

    public StatHuntInboundResponse total(StatHuntSearchRequest search) throws IOException, ResultFailException {
        return getData(subUrl + "total", search, StatHuntInboundResponse.class).getData();
    }
}
