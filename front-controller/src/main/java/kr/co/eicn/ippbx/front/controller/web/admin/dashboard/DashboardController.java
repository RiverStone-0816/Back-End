package kr.co.eicn.ippbx.front.controller.web.admin.dashboard;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.QueueSummaryAndMemberPeers;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.model.search.PersonSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.dashboard.DashboardApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.service.etc.MonitApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.InboundStatApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.statdb.*;
import kr.co.eicn.ippbx.model.search.HuntMonitorSearchRequest;
import kr.co.eicn.ippbx.model.search.MonitControlSearchRequest;
import kr.co.eicn.ippbx.model.search.StatInboundSearchRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/dashboard")
public class DashboardController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private final MonitApiInterface monitApiInterface;
    private final DashboardApiInterface dashboardApiInterface;
    private final QueueApiInterface queueApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final InboundStatApiInterface inboundStatApiInterface;
    private final PartMonitoringApiInterface partMonitoringApiInterface;
    private final UserApiInterface userApiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<DashListResponse> dashboardList = dashboardApiInterface.getDashboardList();
        dashboardList.sort(Comparator.comparingInt(DashListResponse::getDashboardUiSeq));

        final List<DashViewListResponse> dashboardViewList = dashboardApiInterface.getDashboardViewList().stream().filter(e -> e.getDashboardInfoId() != null && e.getSeq() != null).collect(Collectors.toList());
        final List<Integer> showingDashboardIds = dashboardViewList.stream().mapToInt(DashViewListResponse::getDashboardInfoId).distinct().boxed().collect(Collectors.toList());

        final Predicate<DashListResponse> isShow = e -> showingDashboardIds.contains(e.getDashboardId());
        final List<DashListResponse> showingDashboards = dashboardList.stream().filter(isShow).collect(Collectors.toList());
        model.addAttribute("showingDashboards", showingDashboards);

        final List<DashListResponse> noneShowingDashboards = dashboardList.stream().filter(isShow.negate()).collect(Collectors.toList());
        model.addAttribute("noneShowingDashboards", noneShowingDashboards);

        final Map<Integer, DashListResponse> dashboardIdToDashboard = dashboardList.stream().collect(Collectors.toMap(DashListResponse::getDashboardId, e -> e));
        final Map<Integer, DashListResponse> dashboardSequenceToDashboard = dashboardViewList.stream()
                .filter(e -> dashboardIdToDashboard.containsKey(e.getDashboardInfoId()))
                .collect(Collectors.toMap(DashViewListResponse::getSeq, e -> dashboardIdToDashboard.get(e.getDashboardInfoId())));
        model.addAttribute("dashboardSequenceToDashboard", dashboardSequenceToDashboard);

        return "admin/dashboard/ground";
    }

    /**
     * skt향 대시보드
     */
    @GetMapping("total")
    public String mainPage(Model model) throws IOException, ResultFailException {
        model.addAttribute("inboundChart", dashboardApiInterface.dashboardInboundchart().getInboundChat());

        val search = new StatInboundSearchRequest();
        search.setStartDate(search.getEndDate());
        final List<StatInboundTimeResponse<?>> list = inboundStatApiInterface.list(search);
        model.addAttribute("inboundData", list.isEmpty() ? new StatInboundResponse() : list.get(0).getInboundStat());

        final Map<Integer, String> statuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("statuses", statuses);

        val huntMonitorSearchRequest = new HuntMonitorSearchRequest();
        val huntMonitor = partMonitoringApiInterface.getHuntMonitor(huntMonitorSearchRequest);
        model.addAttribute("huntConsultantsStat", huntMonitor);

        final Map<Integer, Integer> statusCountMap = huntMonitor.stream().reduce(new HuntMonitorResponse(), (a, b) -> {
            val result = new HuntMonitorResponse();
            statuses.keySet().forEach(s -> result.getStatusCountMap().put(s, a.getStatusCountMap().getOrDefault(s, 0) + b.getStatusCountMap().getOrDefault(s, 0)));
            return result;
        }).getStatusCountMap();
        model.addAttribute("statusCountMap", statusCountMap);
        model.addAttribute("statusCountMapSum", statusCountMap.values().stream().mapToInt(e -> e).sum());

        final List<ConsultantRecord> ConsultantRecords = new ArrayList<>();
        model.addAttribute("consultantRecords", ConsultantRecords);

        val excellentCSTop = dashboardApiInterface.getExcellentCSTop();

        partMonitoringApiInterface.getIndividualStat().forEach(e -> {
            val record = new ConsultantRecord();
            ReflectionUtils.copy(record, e);

            record.setInSuccess(excellentCSTop.getInSuccessTopTen().stream().filter(e2 -> Objects.equals(e2.getId(), e.getPerson().getId())).mapToInt(StatUserRankingInSuccess::getInSuccess).findAny().orElse(0));
            record.setOutSuccess(excellentCSTop.getOutSuccessTopTen().stream().filter(e2 -> Objects.equals(e2.getId(), e.getPerson().getId())).mapToInt(StatUserRankingOutSuccess::getOutSuccess).findAny().orElse(0));
            record.setInBillsecSum(excellentCSTop.getInBillSecTopTen().stream().filter(e2 -> Objects.equals(e2.getId(), e.getPerson().getId())).mapToInt(StatUserRankingInBillsecSum::getInBillsecSum).findAny().orElse(0));
            record.setOutBillsecSum(excellentCSTop.getOutBillSecTopTen().stream().filter(e2 -> Objects.equals(e2.getId(), e.getPerson().getId())).mapToInt(StatUserRankingOutBillsecSum::getOutBillsecSum).findAny().orElse(0));
            record.setCallbackSuccess(excellentCSTop.getCallbackSuccessTopTen().stream().filter(e2 -> Objects.equals(e2.getId(), e.getPerson().getId())).mapToInt(StatUserRankingCallbackSuccess::getCallbackSuccess).findAny().orElse(0));

            record.setBillsecSum(Math.max(record.getInBillsecSum(), record.getOutBillsecSum()));

            ConsultantRecords.add(record);
        });

        return "admin/dashboard-sheet/ground";
    }

    @GetMapping("script-for-queue-and-person-status")
    public String scriptForQueueAndPersonStatus(Model model) throws IOException, ResultFailException {
        val peerToUserId = new HashMap<String, String>();
        monitApiInterface.listDashboard(new MonitControlSearchRequest()).forEach(e -> e.getPerson().forEach(e2 -> peerToUserId.put(e2.getPeer(), e2.getId())));
        model.addAttribute("peerToUserId", peerToUserId);
        model.addAttribute("peerToIsStat", userApiInterface.list(new PersonSearchRequest()).stream().filter(e -> StringUtils.isNotEmpty(e.getPeer())).collect(Collectors.toMap(PersonSummaryResponse::getPeer, PersonSummaryResponse::getIsStat)));

        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));
        model.addAttribute("peerStatuses", dashboardApiInterface.getCustomWaitMonitor().getQueueMemberList());
        final List<SummaryQueueResponse> queues = queueApiInterface.addQueueNames();
        model.addAttribute("queues", queues.stream().map(e -> {
            try {
                final QueueSummaryAndMemberPeers queue = new QueueSummaryAndMemberPeers(e);
                final DashHuntMonitorResponse huntInfo = dashboardApiInterface.getHuntMonitor(queue.getNumber());
                queue.setWaitingCustomerCount(huntInfo.getCustomWaitCnt());
                queue.setPeers(huntInfo.getQueueMemberList().stream().map(DashQueueMemberResponse::getPeer).collect(Collectors.toSet()));
                return queue;
            } catch (Exception exception) {
                throw new UncheckedIOException(new IOException(exception.getCause()));
            }
        }).collect(Collectors.toList()));

        final Map<String, Set<String>> serviceNumberToQueueName = new HashMap<>();
        queues.forEach(e->{
            final Set<String> queueNames =  serviceNumberToQueueName.computeIfAbsent(e.getSvcNumber(),(k)->new HashSet<>());
            queueNames.add(e.getName());
        });
        model.addAttribute("serviceNumberToQueueName", serviceNumberToQueueName);

        return "admin/dashboard/script-for-queue-and-person-status";
    }

    @GetMapping(value = "component-service-monitor")
    public String componentServiceMonitor(Model model) throws IOException, ResultFailException {
        final DashHuntMonitorResponse customWaitMonitor = dashboardApiInterface.getCustomWaitMonitor();
        model.addAttribute("customWaitMonitor", customWaitMonitor);

        final DashAveByServiceResponse aveByService = dashboardApiInterface.getAveByService();
        model.addAttribute("aveByService", aveByService);

        final DashMonitorByHuntResponse monitorByHunt = dashboardApiInterface.getMonitorByHunt();
        model.addAttribute("monitorByHunt", monitorByHunt);

        final List<ExcellentConsultant> excellentCS = dashboardApiInterface.getExcellentCS();
        model.addAttribute("excellentCS", excellentCS);

        return "admin/dashboard/component-service-monitor";
    }

    @GetMapping(value = "component", params = "type=total_stat")
    public String componentTotalStat(Model model) throws IOException, ResultFailException {
        final DashTotalStatResponse stat = dashboardApiInterface.getTotalStat();
        model.addAttribute("stat", stat);
        return "admin/dashboard/component-total_stat";
    }

    @GetMapping(value = "component", params = "type=service_stat")
    public String componentServiceStat(Model model, @RequestParam String value) throws IOException, ResultFailException {
        final DashServiceStatResponse stat = dashboardApiInterface.getServiceStat(value);
        model.addAttribute("stat", stat);
        return "admin/dashboard/component-service_stat";
    }

    @GetMapping(value = "component", params = "type=total_monitor")
    public String componentTotalMonitor(Model model) throws IOException, ResultFailException {
        final DashHuntMonitorResponse stat = dashboardApiInterface.getTotalMonitor();
        model.addAttribute("stat", stat);
        return "admin/dashboard/component-total_monitor";
    }

    @GetMapping(value = "component", params = "type=hunt_monitor")
    public String componentHuntMonitor(Model model, @RequestParam String value) throws IOException, ResultFailException {
        final DashHuntMonitorResponse stat = dashboardApiInterface.getHuntMonitor(value);
        model.addAttribute("stat", stat);
        model.addAttribute("huntNumber", value);
        return "admin/dashboard/component-hunt_monitor";
    }

    @GetMapping(value = "component", params = "type=current_custom_wait")
    public String componentCurrentCustomWait(Model model) throws IOException, ResultFailException {
        final DashCurrentCustomWaitResponse stat = dashboardApiInterface.getCurrentCustomWait();
        model.addAttribute("stat", stat);
        return "admin/dashboard/component-current_custom_wait";
    }

    @GetMapping(value = "component", params = "type=current_result")
    public String componentCurrentResult(Model model) throws IOException, ResultFailException {
        final DashHuntMonitorResponse stat = dashboardApiInterface.getCurrentResult();
        model.addAttribute("stat", stat);
        return "admin/dashboard/component-current_result";
    }

    @GetMapping(value = "component", params = "type=current_result_call")
    public String componentCurrentResultCall(Model model) throws IOException, ResultFailException {
        final DashCurrentResultCallResponse stat = dashboardApiInterface.getCurrentResultCall();
        model.addAttribute("stat", stat);
        return "admin/dashboard/component-current_result_call";
    }

    @GetMapping(value = "component", params = "type=server_monitor")
    public String componentServerMonitor(Model model, @RequestParam String value) throws IOException, ResultFailException {
        final DashServerMonitorResponse stat = dashboardApiInterface.getServerMonitor(value);
        if (StringUtils.isNotEmpty(stat.getUsedHdd())) {
            model.addAttribute("usedHdd", Integer.parseInt(StringUtils.isNotEmpty(stat.getUsedHdd()) ? stat.getUsedHdd() : "0"));
        }

        model.addAttribute("stat", stat);
        return "admin/dashboard/component-server_monitor";
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ConsultantRecord extends MonitorQueuePersonStatResponse {
        private Integer inSuccess = 0;
        private Integer outSuccess = 0;
        private Integer inBillsecSum = 0;
        private Integer outBillsecSum = 0;
        private Integer callbackSuccess = 0;
        private Integer billsecSum = 0;
    }
}
