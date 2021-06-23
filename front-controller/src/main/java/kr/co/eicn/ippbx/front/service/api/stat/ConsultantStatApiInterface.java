package kr.co.eicn.ippbx.front.service.api.stat;

import com.fasterxml.jackson.databind.JavaType;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.statdb.StatMyCallResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserResponse;
import kr.co.eicn.ippbx.model.dto.util.*;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ConsultantStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ConsultantStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stat/user/";

    @SuppressWarnings("unchecked")
    public List<StatUserResponse<?>> list(StatUserSearchRequest search) throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(List.class,
                typeFactory.constructParametricType(StatUserResponse.class,
                        SearchCycle.DATE.equals(search.getTimeUnit()) ? DateResponse.class
                                : SearchCycle.HOUR.equals(search.getTimeUnit()) ? HourResponse.class
                                : SearchCycle.WEEK.equals(search.getTimeUnit()) ? WeekResponse.class
                                : SearchCycle.MONTH.equals(search.getTimeUnit()) ? MonthResponse.class
                                : SearchCycle.DAY_OF_WEEK.equals(search.getTimeUnit()) ? DayOfWeekResponse.class
                                : DateResponse.class
                )
        );

        return (List<StatUserResponse<?>>) getData(subUrl, search, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }

    public StatUserResponse.UserStat getTotal(StatUserSearchRequest search) throws IOException, ResultFailException {
        return getData(subUrl + "total", search, StatUserResponse.UserStat.class).getData();
    }

    @SneakyThrows
    public StatMyCallResponse myCallStat() {
        return getData(subUrl + "my-call-stat", null, StatMyCallResponse.class).getData();
    }

}
