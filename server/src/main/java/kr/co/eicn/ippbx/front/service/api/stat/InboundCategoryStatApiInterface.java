package kr.co.eicn.ippbx.front.service.api.stat;

import com.fasterxml.jackson.databind.JavaType;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.JsonResult;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatCategoryIvrTreeResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatCategoryResponse;
import kr.co.eicn.ippbx.server.model.dto.util.*;
import kr.co.eicn.ippbx.server.model.enums.SearchCycle;
import kr.co.eicn.ippbx.server.model.search.StatCategorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InboundCategoryStatApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(InboundCategoryStatApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stat/inbound/category/";


    @SuppressWarnings("unchecked")
    public List<StatCategoryResponse<?>> list(StatCategorySearchRequest search) throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(List.class,
                typeFactory.constructParametricType(StatCategoryResponse.class,
                        SearchCycle.DATE.equals(search.getTimeUnit()) ? DateResponse.class
                                : SearchCycle.HOUR.equals(search.getTimeUnit()) ? HourResponse.class
                                : SearchCycle.WEEK.equals(search.getTimeUnit()) ? WeekResponse.class
                                : SearchCycle.MONTH.equals(search.getTimeUnit()) ? MonthResponse.class
                                : SearchCycle.DAY_OF_WEEK.equals(search.getTimeUnit()) ? DayOfWeekResponse.class
                                : DateResponse.class
                )
        );

        return (List<StatCategoryResponse<?>>) getData(subUrl, search, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }

    public List<StatCategoryIvrTreeResponse> ivrTree() throws IOException, ResultFailException {
        return getList(subUrl + "ivr-tree", null, StatCategoryIvrTreeResponse.class).getData();
    }
}
