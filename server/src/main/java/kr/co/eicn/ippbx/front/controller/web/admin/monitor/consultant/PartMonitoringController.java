package kr.co.eicn.ippbx.front.controller.web.admin.monitor.consultant;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.route.CsRouteApiInterface;
import kr.co.eicn.ippbx.front.service.api.dashboard.DashboardApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.QueueMonitoringApiInterface;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.server.model.dto.eicn.ExcellentConsultant;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitorQueuePersonStatResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryQueueResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.*;
import kr.co.eicn.ippbx.server.model.search.HuntMonitorSearchRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/monitor/consultant/part")
public class PartMonitoringController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PartMonitoringController.class);

    private final PartMonitoringApiInterface apiInterface;
    private final QueueMonitoringApiInterface queueMonitoringApiInterface;
    private final CsRouteApiInterface csRouteApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final QueueApiInterface queueApiInterface;
    private final DashboardApiInterface dashboardApiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final CenterStatusResponse centerStat = apiInterface.getCenterStat();
        model.addAttribute("centerStat", centerStat);

        return "admin/monitor/consultant/part/ground";
    }

    @GetMapping("center-stat")
    public String centerStat(Model model) throws IOException, ResultFailException {
        final CenterStatusResponse centerStat = apiInterface.getCenterStat();
        model.addAttribute("stat", centerStat);

        return "admin/monitor/consultant/part/center-stat";
    }

    @GetMapping("primary-stat")
    public String primaryStat(Model model) throws IOException, ResultFailException {
        final CenterStatusResponse centerStat = apiInterface.getCenterStat();
        model.addAttribute("stat", centerStat);

        return "admin/monitor/consultant/part/primary-stat";
    }

    @GetMapping("waiting-number-by-time-chart")
    public String waitingNumberChart(Model model) throws IOException, ResultFailException {
        final TreeMap<Byte, Integer> stat = new TreeMap<>(Comparator.comparing(e -> e));
        stat.putAll(apiInterface.getMaximumNumberOfWaitCountByTime());
        model.addAttribute("stat", stat);

        return "admin/monitor/consultant/part/waiting-number-by-time-chart";
    }

    @GetMapping("hunt-monitor")
    public String huntMonitor(Model model, @ModelAttribute("search") HuntMonitorSearchRequest search) throws IOException, ResultFailException {
        final List<HuntMonitorResponse> list = apiInterface.getHuntMonitor(search);
        model.addAttribute("list", list);

        final Map<Integer, String> statuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("statuses", statuses);

        return "admin/monitor/consultant/part/hunt-monitor";
    }

    @GetMapping("consultant-monitor")
    public String consultantMonitor(Model model) throws IOException, ResultFailException {
        final List<MonitorQueuePersonStatResponse> list = apiInterface.getIndividualStat();
        model.addAttribute("list", list);

        final Map<Integer, String> statuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("statuses", statuses);

        final List<ExcellentConsultant> excellentConsultants = apiInterface.getExcellentCS();
        model.addAttribute("excellentConsultants", excellentConsultants);

        final Map<String, String> excellentConsultantTypes = FormUtils.options(ExcellentConsultant.Type.class);
        model.addAttribute("excellentConsultantTypes", excellentConsultantTypes);

        return "admin/monitor/consultant/part/consultant-monitor";
    }

    @GetMapping("total-stat")
    public String totalStat(Model model) throws IOException, ResultFailException {
        final TotalStatResponse stat = apiInterface.getTotalStat();
        model.addAttribute("stat", stat);

        return "admin/monitor/consultant/part/total-stat";
    }

    @GetMapping("hunt-compare-chart")
    public String huntCompareChart(Model model, @RequestParam(required = false) String queueAName, @RequestParam(required = false) String queueBName) throws IOException, ResultFailException {
        final List<SummaryQueueResponse> queues = queueApiInterface.subGroups();
        model.addAttribute("queues", queues.stream().collect(Collectors.toMap(SummaryQueueResponse::getName, SummaryQueueResponse::getHanName)));

        if (queues.size() > 2) {
            if (StringUtils.isEmpty(queueAName))
                queueAName = queues.get(0).getName();
            if (StringUtils.isEmpty(queueBName))
                queueBName = queues.get(0).getName();

            if (Objects.equals(queueAName, queueBName)) {
                for (SummaryQueueResponse queue : queues) {
                    if (!Objects.equals(queue.getName(), queueBName)) {
                        queueBName = queue.getName();
                        break;
                    }
                }
            }
            model.addAttribute("queueAName", queueAName);
            model.addAttribute("queueBName", queueBName);

            final TotalStatGraphResponse data = apiInterface.getComparedHuntTotalCallCountByTime(queueAName, queueBName);

            final Map<Byte, Map<String, Integer>> hourToQueueNameToCountOfDayMap = new TreeMap<>(Comparator.comparingInt(e -> e));
            model.addAttribute("hourToQueueNameToCountOfDayMap", hourToQueueNameToCountOfDayMap);
            for (byte i = 0; i < 24; i++) {
                final Map<String, Integer> queueNameToCount = hourToQueueNameToCountOfDayMap.computeIfAbsent(i, (key) -> new HashMap<>());
                queueNameToCount.put(data.getHuntAHourData().getQueueName(), data.getHuntAHourData().getDataMap().getOrDefault(i, 0));
                queueNameToCount.put(data.getHuntBHourData().getQueueName(), data.getHuntBHourData().getDataMap().getOrDefault(i, 0));
            }

            final Map<Byte, Map<String, Integer>> hourToQueueNameToCountOfWeekMap = new TreeMap<>(Comparator.comparingInt(e -> e));
            model.addAttribute("hourToQueueNameToCountOfWeekMap", hourToQueueNameToCountOfWeekMap);
            for (byte i = 0; i < 24; i++) {
                final Map<String, Integer> queueNameToCount = hourToQueueNameToCountOfWeekMap.computeIfAbsent(i, (key) -> new HashMap<>());
                queueNameToCount.put(data.getHuntAWeekData().getQueueName(), data.getHuntAWeekData().getDataMap().getOrDefault(i, 0));
                queueNameToCount.put(data.getHuntBWeekData().getQueueName(), data.getHuntBWeekData().getDataMap().getOrDefault(i, 0));
            }
        }

        return "admin/monitor/consultant/part/hunt-compare-chart";
    }

    @GetMapping("hunt-stat")
    public String huntStat(Model model) throws IOException, ResultFailException {
        final List<HuntStatMonitorResponse> list = apiInterface.getHuntStat();
        model.addAttribute("list", list);

        return "admin/monitor/consultant/part/hunt-stat";
    }

    @GetMapping("hunt-hour-chart")
    public String huntHourChart(Model model) throws IOException, ResultFailException {
        final List<SummaryQueueResponse> queues = queueApiInterface.subGroups();
        model.addAttribute("queues", queues.stream().collect(Collectors.toMap(SummaryQueueResponse::getName, SummaryQueueResponse::getHanName)));

        final Map<String, Map<Byte, Integer>> queueNameToDataMap = apiInterface.getHuntTotalCallCountByTime().stream().collect(Collectors.toMap(StatHourGraphData::getQueueName, StatHourGraphData::getDataMap));
        final Map<Byte, Map<String, Integer>> hourToQueueNameToCountMap = new TreeMap<>(Comparator.comparingInt(e -> e));
        model.addAttribute("hourToQueueNameToCountMap", hourToQueueNameToCountMap);
        for (byte i = 0; i < 24; i++) {
            final Map<String, Integer> queueNameToCount = hourToQueueNameToCountMap.computeIfAbsent(i, (key) -> new HashMap<>());
            for (SummaryQueueResponse queue : queues) {
                final Map<Byte, Integer> orDefault = queueNameToDataMap.getOrDefault(queue.getName(), new HashMap<>());
                queueNameToCount.put(queue.getName(), orDefault.getOrDefault(i, 0));
            }
        }

        return "admin/monitor/consultant/part/hunt-hour-chart";
    }
}
