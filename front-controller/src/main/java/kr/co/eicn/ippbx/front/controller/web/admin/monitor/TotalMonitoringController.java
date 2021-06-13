package kr.co.eicn.ippbx.front.controller.web.admin.monitor;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.QueueMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.InboundStatApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.dto.eicn.MonitorQueueSummaryResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatInboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatInboundTimeResponse;
import kr.co.eicn.ippbx.model.dto.util.HourResponse;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatInboundSearchRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/monitor/total")
public class TotalMonitoringController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TotalMonitoringController.class);

    private final QueueMonitoringApiInterface queueMonitoringApiInterface;
    private final PartMonitoringApiInterface partMonitoringApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final ScreenDataApiInterface screenDataApiInterface;
    private final InboundStatApiInterface inboundStatApiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        return "admin/monitor/total/ground";
    }

    @SneakyThrows
    @GetMapping("queue-stat")
    public String queueStat(Model model) {
        final List<MonitorQueueSummaryResponse> queueSummaries = queueMonitoringApiInterface.getSummary();
        model.addAttribute("queueSummaries", queueSummaries);

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("memberStatuses", memberStatuses);

        return "admin/monitor/total/queue-stat";
    }

    @SneakyThrows
    @GetMapping("service-stat")
    public String serviceStat(Model model) {
        model.addAttribute("data", screenDataApiInterface.byHunt());
        model.addAttribute("statusCodes", companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName)));

        return "admin/monitor/total/service-stat";
    }

    @SneakyThrows
    @GetMapping("yesterday-today-comparing-chart")
    public String todayAndYesterdayInboundComparingChart(Model model) {

        val search = new StatInboundSearchRequest();
        search.setTimeUnit(SearchCycle.HOUR);
        search.setStartDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        search.setEndDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        final List<StatInboundTimeResponse<?>> yesterdayData = inboundStatApiInterface.list(search);

        search.setStartDate(new Date(System.currentTimeMillis()));
        search.setEndDate(new Date(System.currentTimeMillis()));
        final List<StatInboundTimeResponse<?>> todayData = inboundStatApiInterface.list(search);

        final TreeMap<Integer, Pair<StatInboundResponse, StatInboundResponse>> data = new TreeMap<>(Comparator.comparing(e -> e));

        yesterdayData.forEach(e -> {
            Integer hour = Integer.valueOf(((HourResponse) e.getTimeInformation()).getHour());
            Pair<StatInboundResponse, StatInboundResponse> datum = data.getOrDefault(hour, new MutablePair<>());
            data.put(hour, new MutablePair<>(e.getInboundStat(), datum.getRight()));
        });

        todayData.forEach(e -> {
            Integer hour = Integer.valueOf(((HourResponse) e.getTimeInformation()).getHour());
            Pair<StatInboundResponse, StatInboundResponse> datum = data.getOrDefault(hour, new MutablePair<>());
            data.put(hour, new MutablePair<>(datum.getLeft(), e.getInboundStat()));
        });

        model.addAttribute("data", data);

        return "admin/monitor/total/yesterday-today-comparing-chart";
    }
}
