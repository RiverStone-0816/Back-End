package kr.co.eicn.ippbx.front.controller.api.monitor.consultant;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.model.dto.statdb.*;
import kr.co.eicn.ippbx.model.search.HuntMonitorSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/part-monitoring", produces = MediaType.APPLICATION_JSON_VALUE)
public class PartMonitoringApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PartMonitoringApiController.class);

    private final PartMonitoringApiInterface apiInterface;

    /**
     * 센터현황별 모니터링
     */
    @GetMapping("center")
    public CenterStatusResponse getCenterStat() throws IOException, ResultFailException {
        return apiInterface.getCenterStat();
    }

    /**
     * 시간별 최대 대기자수
     */
    @GetMapping("hour")
    public Map<Byte, Integer> getNumberOfWaitHour() throws IOException, ResultFailException {
        return apiInterface.getMaximumNumberOfWaitCountByTime();
    }

    /**
     * 큐그룹별 모니터링
     **/
    @GetMapping("hunt-monitor")
    public List<HuntMonitorResponse> getHuntMonitor(HuntMonitorSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.getHuntMonitor(search);
    }

    /**
     * 통합통계모니터링
     */
    @GetMapping("total-stat")
    public TotalStatResponse getTotalStat() throws IOException, ResultFailException {
        return apiInterface.getTotalStat();
    }

    /**
     * 큐그룹별 통계 모니터링
     */
    @GetMapping("hunt-stat")
    public List<HuntStatMonitorResponse> getHuntStat() throws IOException, ResultFailException {
        return apiInterface.getHuntStat();
    }
}
