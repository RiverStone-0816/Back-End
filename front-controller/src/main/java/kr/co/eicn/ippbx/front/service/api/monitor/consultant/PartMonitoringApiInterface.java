package kr.co.eicn.ippbx.front.service.api.monitor.consultant;

import com.fasterxml.jackson.databind.JavaType;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ExcellentConsultant;
import kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse;
import kr.co.eicn.ippbx.model.dto.statdb.*;
import kr.co.eicn.ippbx.model.search.HuntMonitorSearchRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PartMonitoringApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PartMonitoringApiInterface.class);
    private static final String subUrl = "/api/v1/admin/monitor/consultant/part/";

    public CenterStatusResponse getCenterStat() throws IOException, ResultFailException {
        return getData(subUrl + "center", null, CenterStatusResponse.class).getData();
    }

    @SuppressWarnings("unchecked")
    public Map<Byte, Integer> getMaximumNumberOfWaitCountByTime() throws IOException, ResultFailException {
        final JavaType type = typeFactory.constructParametricType(Map.class, Byte.class, Integer.class);
        return (Map<Byte, Integer>) getData(subUrl + "hour", null, typeFactory.constructParametricType(JsonResult.class, type)).getData();
    }

    public List<HuntMonitorResponse> getHuntMonitor(HuntMonitorSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "hunt-monitor", search, HuntMonitorResponse.class).getData();
    }

    public List<ExcellentConsultant> getExcellentCS() throws IOException, ResultFailException {
        return getList(subUrl + "excellent-consultant", null, ExcellentConsultant.class).getData();
    }

    public TotalStatResponse getTotalStat() throws IOException, ResultFailException {
        return getData(subUrl + "total-stat", null, TotalStatResponse.class).getData();
    }

    public List<HuntStatMonitorResponse> getHuntStat() throws IOException, ResultFailException {
        return getList(subUrl + "hunt-stat", null, HuntStatMonitorResponse.class).getData();
    }

    public TotalStatGraphResponse getComparedHuntTotalCallCountByTime(String queueAName, String queueBName) throws IOException, ResultFailException {
        return getData(subUrl + "compared-hunt-total-call-count-by-time", UrlUtils.makeParamMap("queueAName", queueAName, "queueBName", queueBName), TotalStatGraphResponse.class).getData();
    }

    public List<StatHourGraphData> getHuntTotalCallCountByTime() throws IOException, ResultFailException {
        return getList(subUrl + "hunt-total-call-count-by-time", null, StatHourGraphData.class).getData();
    }

    public List<MonitorQueuePersonStatResponse> getIndividualStat() throws IOException, ResultFailException {
        return getList(subUrl + "individual-stat", null, MonitorQueuePersonStatResponse.class).getData();
    }
}
