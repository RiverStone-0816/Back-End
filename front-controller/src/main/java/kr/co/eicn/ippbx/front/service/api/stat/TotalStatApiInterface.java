package kr.co.eicn.ippbx.front.service.api.stat;

import com.fasterxml.jackson.databind.JavaType;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.StatTotalRow;
import kr.co.eicn.ippbx.model.dto.util.*;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatTotalSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TotalStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TotalStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stat/total/";

    @SuppressWarnings("unchecked")
    public List<StatTotalRow<?>> list(StatTotalSearchRequest search) throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(List.class,
                typeFactory.constructParametricType(StatTotalRow.class,
                        SearchCycle.DATE.equals(search.getTimeUnit()) ? DateResponse.class
                                : SearchCycle.HOUR.equals(search.getTimeUnit()) ? HourResponse.class
                                : SearchCycle.WEEK.equals(search.getTimeUnit()) ? WeekResponse.class
                                : SearchCycle.MONTH.equals(search.getTimeUnit()) ? MonthResponse.class
                                : SearchCycle.DAY_OF_WEEK.equals(search.getTimeUnit()) ? DayOfWeekResponse.class
                                : DateResponse.class
                )
        );

        return (List<StatTotalRow<?>>) getData(subUrl, search, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }

    @SuppressWarnings("unchecked")
    public StatTotalRow<DateResponse> total(StatTotalSearchRequest search) throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(StatTotalRow.class, DateResponse.class);
        return (StatTotalRow<DateResponse>) getData(subUrl + "total", search, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }
}
