package kr.co.eicn.ippbx.server.controller.api.v1.admin.dashboard;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserRankingResponse;
import kr.co.eicn.ippbx.model.enums.ServiceKind;
import kr.co.eicn.ippbx.model.form.DashboardViewListFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.*;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName.QUEUE_NAME;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 상담톡관리 > 상담톡정보관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardApiController extends ApiBaseController {
    private final DaemonRepository daemonRepository;
    private final ServiceRepository serviceRepository;
    private final QueueNameRepository queueRepository;
    private final ServerInfoRepository serverInfoRepository;
    private final DashboardInfoRepository dashboardInfoRepository;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final DashboardViewListRepository dashboardViewListRepository;
    private final CurrentMemberStatusRepository currentMemberStatusRepository;

    private final StatInboundService statInboundService;
    private final StatOutboundService statOutboundService;
    private final StatQueueWaitService statQueueWaitService;
    private final StatUserRankingService statUserRankingService;
    private final EicnMonitDataService eicnMonitDataService;

    /**
     * 고객대기자수 모니터링
     */
    @GetMapping("custom-wait-monitor")
    public ResponseEntity<JsonResult<DashHuntMonitorResponse>> getCustomWaitMonitor() {
        final DashHuntMonitorResponse response = queueMemberTableRepository.getDashHuntMonitor("");

        return ResponseEntity.ok(data(response));
    }

    /**
     * 대표서비스별 응답률
     */
    @GetMapping("ave-by-service")
    public ResponseEntity<JsonResult<DashAveByServiceResponse>> getAveByService() {
        final DashAveByServiceResponse response = new DashAveByServiceResponse();

        final List<DashServiceStatResponse> svc_stat_list = serviceRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(ServiceList::getSvcName).reversed().thenComparing(ServiceList::getSvcNumber))
                .map(e -> {
                    final DashServiceStatResponse response1 = statInboundService.getRepository().getDashServiceStat(e.getSvcNumber(), "");
                    response1.setSvcName(e.getSvcName());
                    return response1;
                })
                .collect(Collectors.toList());

        response.setServiceListStat(svc_stat_list);

        return ResponseEntity.ok(data(response));
    }

    /**
     * 큐그룹별 모니터링
     */
    @GetMapping("monitor-by-hunt")
    public ResponseEntity<JsonResult<DashMonitorByHuntResponse>> getMonitorByHunt() {
        final DashMonitorByHuntResponse response = new DashMonitorByHuntResponse();

        final List<DashHuntMonitorResponse> hunt_list = queueRepository.findAll(QUEUE_NAME.NAME.like("QUEUE%"))
                .stream()
                .sorted(Comparator.comparing(QueueName::getHanName).reversed().thenComparing(QueueName::getName))
                .map(e -> {
                    final DashHuntMonitorResponse response1 = queueMemberTableRepository.getDashHuntMonitor(e.getName());
                    response1.setQueueHanName(e.getHanName());
                    return response1;
                })
                .collect(Collectors.toList());

        response.setHuntList(hunt_list);

        return ResponseEntity.ok(data(response));
    }

    /**
     * 우수실적상담원
     */
    @GetMapping("excellent-cs")
    public ResponseEntity<JsonResult<List<ExcellentConsultant>>> getExcellentCS() {
        StatUserRankingResponse mostReceive = statUserRankingService.getRepository().getExcellentConsultantList(ExcellentConsultant.Type.MOST_RECEIVE);
        StatUserRankingResponse mostSend = statUserRankingService.getRepository().getExcellentConsultantList(ExcellentConsultant.Type.MOST_SEND);
        StatUserRankingResponse longestReceive = statUserRankingService.getRepository().getExcellentConsultantList(ExcellentConsultant.Type.LONGEST_RECEIVE);
        StatUserRankingResponse longestSend = statUserRankingService.getRepository().getExcellentConsultantList(ExcellentConsultant.Type.LONGEST_SEND);
        StatUserRankingResponse longestCall = statUserRankingService.getRepository().getExcellentConsultantList(ExcellentConsultant.Type.LONGEST_CALL);
        StatUserRankingResponse mostCallback = statUserRankingService.getRepository().getExcellentConsultantList(ExcellentConsultant.Type.MOST_CALLBACK);

        final List<ExcellentConsultant> list = new ArrayList<>();

        if (mostReceive != null)
            list.add(new ExcellentConsultant(ExcellentConsultant.Type.MOST_RECEIVE, mostReceive.getIdName(), mostReceive.getInSuccess(), mostReceive.getInBillsecSum()));

        if (mostSend != null)
            list.add(new ExcellentConsultant(ExcellentConsultant.Type.MOST_SEND, mostSend.getIdName(), mostSend.getOutSuccess(), mostSend.getOutBillsecSum()));

        if (longestReceive != null)
            list.add(new ExcellentConsultant(ExcellentConsultant.Type.LONGEST_RECEIVE, longestReceive.getIdName(), longestReceive.getInSuccess(), longestReceive.getInBillsecSum()));

        if (longestSend != null)
            list.add(new ExcellentConsultant(ExcellentConsultant.Type.LONGEST_SEND, longestSend.getIdName(), longestSend.getOutSuccess(), longestSend.getOutBillsecSum()));

        if (longestCall != null)
            list.add(new ExcellentConsultant(ExcellentConsultant.Type.LONGEST_CALL, longestCall.getIdName(), longestCall.getTotalSuccess(), longestCall.getTotalBillsecSum()));

        if (mostCallback != null)
            list.add(new ExcellentConsultant(ExcellentConsultant.Type.MOST_CALLBACK, mostCallback.getIdName(), mostCallback.getCallbackSuccess(), mostCallback.getTotalBillsecSum()));

        return ResponseEntity.ok(data(list));
    }

    /**
     * 통합통계
     */
    @GetMapping("total-stat")
    public ResponseEntity<JsonResult<DashTotalStatResponse>> getTotalStat() {
        final DashServiceStatResponse statRes = statInboundService.getRepository().getDashServiceStat("", "");
        final DashTotalStatResponse response = convertDto(statRes, DashTotalStatResponse.class);

        return ResponseEntity.ok(data(response));
    }

    /**
     * 대표서비스 통계
     */
    @GetMapping("service-stat/{number}")
    public ResponseEntity<JsonResult<DashServiceStatResponse>> getServiceStat(@PathVariable String number) {

        final DashServiceStatResponse response = statInboundService.getRepository().getDashServiceStat(number, "");
        final List<ServiceList> serviceLists = serviceRepository.findAll();
        serviceLists.stream().filter(e -> e.getSvcNumber().equals(number)).findFirst()
                .ifPresent(e -> response.setSvcName(e.getSvcName()));

        return ResponseEntity.ok(data(response));
    }

    /**
     * 통합 모니터링
     */
    @GetMapping("total-monitor")
    public ResponseEntity<JsonResult<DashHuntMonitorResponse>> getTotalMonitor() {
        final DashHuntMonitorResponse response = queueMemberTableRepository.getDashHuntMonitor("");
        response.setRateValue(statInboundService.getRepository().getDashServiceStat("", "").getRateValue());

        return ResponseEntity.ok(data(response));
    }

    /**
     * 큐그룹별 모니터링
     */
    @GetMapping("hunt-monitor/{number}")
    public ResponseEntity<JsonResult<DashHuntMonitorResponse>> getHuntMonitor(@PathVariable String number) {

        final DashHuntMonitorResponse[] response = {null};
        final List<QueueName> queueNameList = queueRepository.findAll();
        queueNameList.stream().filter(e -> e.getNumber().equals(number)).findFirst()
                .ifPresent(e -> {
                    response[0] = queueMemberTableRepository.getDashHuntMonitor(e.getName());
                    response[0].setQueueHanName(e.getHanName());
                    response[0].setRateValue(
                            statInboundService.getRepository().getDashServiceStat("", number).getRateValue()
                    );
                });

        return ResponseEntity.ok(data(response[0]));
    }

    /**
     * 실시간 고객대기
     */
    @GetMapping("current-custom-wait")
    public ResponseEntity<JsonResult<DashCurrentCustomWaitResponse>> getCurrentCustomWait() {
        final DashCurrentCustomWaitResponse response = statInboundService.getRepository().getDashCurrentWait("", "");
        return ResponseEntity.ok(data(response));
    }

    /**
     * 실시간 상담현황
     */
    @GetMapping("current-result")
    public ResponseEntity<JsonResult<DashHuntMonitorResponse>> getCurrentResult() {
        final DashHuntMonitorResponse response = queueMemberTableRepository.getDashHuntMonitor("");

        final Calendar current = Calendar.getInstance();
        int currentHour = current.get(Calendar.HOUR_OF_DAY);
        Map<Byte, DashResultChartResponse> graphData = statQueueWaitService.getRepository().getHourToMaxWaitCnt();

        for (int i = 6; i > 0; i--) {
            final DashResultChartResponse chartResponseList = new DashResultChartResponse();
            byte hour = (byte) (currentHour - i + 1);
            if (graphData.get(hour) != null) {
                chartResponseList.setMaxWaitCnt(graphData.get(hour).getMaxWaitCnt());
            } else {
                chartResponseList.setMaxWaitCnt(0);
            }
            response.getHourToCurrentCnt().put(currentHour - i + 1, chartResponseList);
        }

        return ResponseEntity.ok(data(response));
    }

    /**
     * 실시간 상담통화
     */
    @GetMapping("current-result-call")
    public ResponseEntity<JsonResult<DashCurrentResultCallResponse>> getCurrentResultCall() {
        final DashCurrentResultCallResponse response = currentMemberStatusRepository.getCurrentResultCall();
        response.setTitle("실시간 상담통화");

        final Calendar current = Calendar.getInstance();
        int currentHour = current.get(Calendar.HOUR_OF_DAY);
        Map<Byte, DashServiceStatResponse> graphInboundData = statInboundService.getRepository().getHourToInbound("", "");
        Map<Byte, DashServiceStatResponse> graphOutboundData = statOutboundService.getRepository().getHourOutInbound();

        for (int i = 6; i > 0; i--) {
            final DashResultCallChartResponse chartResponseList = new DashResultCallChartResponse();
            byte hour = (byte) (currentHour - i + 1);
            if (graphInboundData.get(hour) != null) {
                chartResponseList.setInboundCnt(graphInboundData.get(hour).getSuccessCnt());
            } else {
                chartResponseList.setInboundCnt(0);
            }
            if (graphOutboundData.get(hour) != null) {
                chartResponseList.setOutboundCnt(graphOutboundData.get(hour).getSuccessCnt());
            } else {
                chartResponseList.setOutboundCnt(0);
            }
            response.getHourToResultCall().put(currentHour - i + 1, chartResponseList);
        }

        return ResponseEntity.ok(data(response));
    }

    /**
     * 서버 모니터링
     */
    @GetMapping("server-monitor/{host}")
    public ResponseEntity<JsonResult<DashServerMonitorResponse>> getServerMonitor(@PathVariable String host) {
        final DashServerMonitorResponse response = new DashServerMonitorResponse();

        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(host)).findFirst()
                .ifPresent(server -> response.setTitle(server.getName() + " 모니터링"));

        eicnMonitDataService.runExecuteServerMonit();
        final List<String> serverData = eicnMonitDataService.getMonitData(host);

        final List<DashServerMonitorDaemonListResponse> daemonList = new ArrayList<>();
        serverData.forEach(data -> {
            String[] info = data.split(":");
            String daemonNames = "";
            String daemonDatas = "";
            if (info.length >= 2) {
                daemonNames = info[0];
                daemonDatas = info[1];
            }
            if (data.contains("cpu_idle")) {
                response.setUsedCpu(daemonDatas + "%");
            }
            if (data.contains("memory")) {
                response.setUsedMemory(daemonDatas + "%");
            }
//			if(data.contains("disk_use")) {
//				response.setUsedHdd(daemonDatas);
//			}
            if (data.contains("disk_percent")) {
                String s = daemonDatas.split(" ")[1];
                response.setUsedHdd(s.substring(0, s.indexOf('%')));
            }
            if (data.contains("daemon_")) {
                int idx = daemonNames.indexOf("_");
                String daemonId = daemonNames.substring(idx + 1);
                String daemonStatus = "정상";
                if (!daemonDatas.contains("STATE OK")) {
                    daemonStatus = "비정상";
                }
                final DashServerMonitorDaemonListResponse daemon = new DashServerMonitorDaemonListResponse();
                if (daemonId.equals("mysqld")) {
                    daemonId = "mysql";
                }
                String daemonName = daemonRepository.getDaemonName(daemonId);
                if (daemonName == null) {
                    daemonName = daemonId;
                }
                daemon.setDaemonName(daemonName);
                daemon.setDaemonStatus(daemonStatus);
                daemonList.add(daemon);
            }
        });
        response.setDaemonList(daemonList);

        return ResponseEntity.ok(data(response));
    }

    /**
     * 대시보드 리스트
     */
    @GetMapping("dashboard-list")
    public ResponseEntity<JsonResult<List<DashListResponse>>> getDashboardList(@RequestParam String serviceKind) {
        boolean isCloud = serviceKind.equals(ServiceKind.CC.getCode());

        List<DashListResponse> list = dashboardInfoRepository.getDashboardList(isCloud);

        return ResponseEntity.ok(data(list));
    }

    /**
     * 대시보드 View 리스트
     */
    @GetMapping("dashboard-view-list")
    public ResponseEntity<JsonResult<List<DashViewListResponse>>> getDashboardViewList() {
        final List<DashViewListResponse> list = dashboardViewListRepository.findAll().stream()
                .map((e) -> convertDto(e, DashViewListResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    /**
     * 대시보드 View 추가
     */
    @PostMapping
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody DashboardViewListFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);
        form.setCompanyId(g.getUser().getCompanyId());
        dashboardViewListRepository.insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/dashboard")).body(create());
    }

    /**
     * 대시보드 View 수정
     */
    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody DashboardViewListFormRequest form, BindingResult bindingResult,
                                                @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);
        form.setCompanyId(g.getUser().getCompanyId());
        dashboardViewListRepository.updateByKeySeqAndComapnyId(form, seq);
        return ResponseEntity.ok(create());
    }

    /**
     * 대시보드 View 삭제
     */
    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        dashboardViewListRepository.deleteByKeySeqAndComapnyId(seq);
        return ResponseEntity.ok(create());
    }

    /**
     * 인바운드 현황
    * */
    @GetMapping("dashboard-inboundchart")
    public ResponseEntity<JsonResult<DashInboundChartResponse>> dashboardInboundchart(){
        return ResponseEntity.ok(data(statInboundService.getRepository().getInboundChartResponseMap()));
    }
}
