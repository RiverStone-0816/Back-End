package kr.co.eicn.ippbx.front.controller.api.dashboard;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.dashboard.DashboardApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.DashboardViewListFormRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardApiController.class);

    private final DashboardApiInterface apiInterface;

    /**
     * 고객대기자수 모니터링
     */
    @GetMapping("custom-wait-monitor")
    public DashHuntMonitorResponse getCustomWaitMonitor() throws IOException, ResultFailException {
        return apiInterface.getCustomWaitMonitor();
    }

    /**
     * 대표서비스별 응답률
     */
    @GetMapping("ave-by-service")
    public DashAveByServiceResponse getAveByService() throws IOException, ResultFailException {
        return apiInterface.getAveByService();
    }

    /**
     * 큐그룹별 모니터링
     */
    @GetMapping("monitor-by-hunt")
    public DashMonitorByHuntResponse getMonitorByHunt() throws IOException, ResultFailException {
        return apiInterface.getMonitorByHunt();
    }

    /**
     * 우수실적상담원
     */
    @GetMapping("excellent-cs")
    public List<ExcellentConsultant> getExcellentCS() throws IOException, ResultFailException {
        return apiInterface.getExcellentCS();
    }

    /**
     * 통합통계
     */
    @GetMapping("total-stat")
    public DashTotalStatResponse getTotalStat() throws IOException, ResultFailException {
        return apiInterface.getTotalStat();
    }

    /**
     * 대표서비스 통계
     */
    @GetMapping("service-stat/{number}")
    public DashServiceStatResponse getServiceStat(@PathVariable String number) throws IOException, ResultFailException {
        return apiInterface.getServiceStat(number);
    }

    /**
     * 통합 모니터링
     */
    @GetMapping("total-monitor")
    public DashHuntMonitorResponse getTotalMonitor() throws IOException, ResultFailException {
        return apiInterface.getTotalMonitor();
    }

    /**
     * 큐그룹별 모니터링
     */
    @GetMapping("hunt-monitor/{number}")
    public DashHuntMonitorResponse getHuntMonitor(@PathVariable String number) throws IOException, ResultFailException {
        return apiInterface.getHuntMonitor(number);
    }

    /**
     * 실시간 고객대기
     */
    @GetMapping("current-custom-wait")
    public DashCurrentCustomWaitResponse getCurrentCustomWait() throws IOException, ResultFailException {
        return apiInterface.getCurrentCustomWait();
    }

    /**
     * 실시간 상담현황
     */
    @GetMapping("current-result")
    public DashHuntMonitorResponse getCurrentResult() throws IOException, ResultFailException {
        return apiInterface.getCurrentResult();
    }

    /**
     * 실시간 상담통화
     */
    @GetMapping("current-result-call")
    public DashCurrentResultCallResponse getCurrentResultCall() throws IOException, ResultFailException {
        return apiInterface.getCurrentResultCall();
    }

    /**
     * 서버 모니터링
     */
    @GetMapping("server-monitor/{host}")
    public DashServerMonitorResponse getServerMonitor(@PathVariable String host) throws IOException, ResultFailException {
        return apiInterface.getServerMonitor(host);
    }

    /**
     * 대시보드 리스트
     */
    @GetMapping("dashboard-list")
    public List<DashListResponse> getDashboardList() throws IOException, ResultFailException {
        return apiInterface.getDashboardList();
    }

    /**
     * 대시보드 View 리스트
     */
    @GetMapping("dashboard-view-list")
    public List<DashViewListResponse> getDashboardViewList() throws IOException, ResultFailException {
        return apiInterface.getDashboardViewList();
    }

    /**
     * 대시보드 View 추가
     */
    @PostMapping
    public void post(@Valid @RequestBody DashboardViewListFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 대시보드 View 수정
     */
    @PutMapping(value = "{seq}")
    public void put(@Valid @RequestBody DashboardViewListFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    /**
     * 대시보드 View 삭제
     */
    @DeleteMapping(value = "{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    /**
     * 인바운드 현황
     */
    @GetMapping("dashboard-inboundchart")
    public DashInboundChartResponse dashboardInboundchart() {
        return apiInterface.dashboardInboundchart();
    }
}
