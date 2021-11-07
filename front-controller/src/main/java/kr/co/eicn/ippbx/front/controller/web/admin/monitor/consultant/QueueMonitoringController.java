package kr.co.eicn.ippbx.front.controller.web.admin.monitor.consultant;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.QueueMonitoringApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/monitor/consultant/queue")
public class QueueMonitoringController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QueueMonitoringController.class);

    private final QueueMonitoringApiInterface queueMonitoringApiInterface;
    private final PartMonitoringApiInterface partMonitoringApiInterface;
    private final CompanyApiInterface companyApiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<MonitorQueueSummaryResponse> queueSummaries = queueMonitoringApiInterface.getSummary();
        model.addAttribute("queueSummaries", queueSummaries);

        final List<MonitorQueueSummaryPerson> queuePersonStatus = queueMonitoringApiInterface.getPersonStatus();
        model.addAttribute("queuePersonStatus", queuePersonStatus);

        final List<MonitorQueuePersonStatResponse> list = partMonitoringApiInterface.getIndividualStat();
        model.addAttribute("list", list);

        final MonitorQueueTotalResponse queueStats = queueMonitoringApiInterface.getStatSummary();
        model.addAttribute("queueStats", queueStats);

        final Map<String, String> queues = queueStats.getStatList().stream().collect(Collectors.toMap(MonitorQueueStatResponse::getQueueName, MonitorQueueStatResponse::getQueueHanName));
        model.addAttribute("queues", queues);

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("memberStatuses", memberStatuses);

        return "admin/monitor/consultant/queue/ground";
    }
}
