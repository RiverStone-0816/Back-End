package kr.co.eicn.ippbx.front.controller.web.admin.monitor;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ChattingApiInterface;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.QueueMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenDataApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.ConsultantStatApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserResponse;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/monitor/consultant-status")
public class ConstantStatusMonitoringController extends BaseController {
    private final QueueMonitoringApiInterface queueMonitoringApiInterface;
    private final PartMonitoringApiInterface partMonitoringApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final ScreenDataApiInterface screenDataApiInterface;
    private final ChattingApiInterface chattingApiInterface;
    private final ConsultantStatApiInterface consultantStatApiInterface;

    @SneakyThrows
    @GetMapping("part")
    public String part(Model model) throws IOException, ResultFailException {
        val statUserSearchRequest = new StatUserSearchRequest();
        statUserSearchRequest.setStartDate(statUserSearchRequest.getEndDate());
        val userStats = consultantStatApiInterface.list(statUserSearchRequest).get(0).getUserStatList();
        val personIdToStat = partMonitoringApiInterface.getIndividualStat().stream().collect(Collectors.toMap(e -> e.getPerson().getId(), e -> e));

        final Map<String, String> groups = new HashMap<>();
        model.addAttribute("groups", groups);
        userStats.forEach(e -> groups.put(e.getGroupCode(), e.getGroupName()));

        final Map<String, List<PersonStatus>> groupToPersons = new HashMap<>();
        model.addAttribute("groupToPersons", groupToPersons);
        userStats.forEach(e -> {
            final List<PersonStatus> list = groupToPersons.getOrDefault(e.getGroupCode(), new ArrayList<>());
            val personStat = personIdToStat.get(e.getUserId());
            if (personStat == null)
                return;

            val personStatus = new PersonStatus();
            personStatus.setId(e.getUserId());
            personStatus.setIdName(e.getIdName());
            personStatus.setExtension(personStat.getPerson().getExtension());
            personStatus.setPeer(personStat.getPerson().getPeer());
            personStatus.setPaused(personStat.getPerson().getPaused());
            personStatus.setIsLogin(personStat.getPerson().getIsLogin());
            personStatus.setCustomNumber(personStat.getCustomNumber());

            personStatus.setInboundTotal(e.getInboundStat().getTotal());
            personStatus.setInboundSuccess(e.getInboundStat().getSuccess());

            personStatus.setOutboundTotal(e.getOutboundStat().getOutTotal());
            personStatus.setOutboundSuccess(e.getOutboundStat().getOutSuccess());

            list.add(personStatus);
            groupToPersons.put(e.getGroupCode(), list);
        });

        val emptyGroups = groups.keySet().stream().filter(groupCode -> groupToPersons.get(groupCode) == null).collect(Collectors.toList());
        emptyGroups.forEach(groups::remove);

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("memberStatuses", memberStatuses);

        val groupToStatusToCount = new HashMap<String, Map<Integer, Integer>>();
        model.addAttribute("groupToStatusToCount", groupToStatusToCount);
        groupToPersons.forEach((groupCode, list) -> {
            final Map<Integer, Integer> statusToCount = groupToStatusToCount.getOrDefault(groupCode, new HashMap<>());
            list.forEach(p -> statusToCount.put(p.getPaused(), statusToCount.getOrDefault(p.getPaused(), 0) + 1));
            groupToStatusToCount.put(groupCode, statusToCount);
        });

        return "admin/monitor/consultant-status/part/ground";
    }

    @GetMapping("queue")
    public String queue(Model model) throws IOException, ResultFailException {
        final List<MonitorQueuePersonStatResponse> personStatuses = queueMonitoringApiInterface.getIndividualStat();

        final Map<String, String> queues = new HashMap<>();
        model.addAttribute("queues", queues);
        personStatuses.stream().filter(e -> StringUtils.isNotEmpty(e.getQueueName())).forEach(e -> queues.put(e.getQueueName(), e.getQueueHanName()));

        val statUserSearchRequest = new StatUserSearchRequest();
        statUserSearchRequest.setStartDate(statUserSearchRequest.getEndDate());
        val personIdToStat = consultantStatApiInterface.list(statUserSearchRequest).get(0).getUserStatList().stream().collect(Collectors.toMap(StatUserResponse.UserStat::getUserId, e -> e));

        final Map<String, List<PersonStatus>> queueToPersons = new HashMap<>();
        model.addAttribute("queueToPersons", queueToPersons);

        personStatuses.stream().filter(e -> StringUtils.isNotEmpty(e.getQueueName())).forEach(e -> {
            val list = queueToPersons.getOrDefault(e.getQueueName(), new ArrayList<>());
            val stat = personIdToStat.get(e.getPerson().getId());

            val personStatus = new PersonStatus();
            personStatus.setId(e.getPerson().getId());
            personStatus.setIdName(e.getPerson().getIdName());
            personStatus.setExtension(e.getPerson().getExtension());
            personStatus.setPeer(e.getPerson().getPeer());
            personStatus.setPaused(e.getPerson().getPaused());
            personStatus.setIsLogin(e.getPerson().getIsLogin());
            personStatus.setCustomNumber(e.getCustomNumber());

            personStatus.setInboundTotal(stat.getInboundStat().getTotal());
            personStatus.setInboundSuccess(stat.getInboundStat().getSuccess());

            personStatus.setOutboundTotal(stat.getOutboundStat().getOutTotal());
            personStatus.setOutboundSuccess(stat.getOutboundStat().getOutSuccess());

            list.add(personStatus);
            queueToPersons.put(e.getQueueName(), list);
        });

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("memberStatuses", memberStatuses);

        val queueToStatusToCount = new HashMap<String, Map<Integer, Integer>>();
        model.addAttribute("queueToStatusToCount", queueToStatusToCount);
        queueToPersons.forEach((queueName, list) -> {
            final Map<Integer, Integer> statusToCount = queueToStatusToCount.getOrDefault(queueName, new HashMap<>());
            list.forEach(p -> statusToCount.put(p.getPaused(), statusToCount.getOrDefault(p.getPaused(), 0) + 1));
            queueToStatusToCount.put(queueName, statusToCount);
        });

        return "admin/monitor/consultant-status/queue/ground";
    }

    @Data
    public static class PersonStatus {
        private String id;
        private String idName;
        private String extension;
        private String peer;
        private Integer paused = 0;
        private String isLogin = "N";

        private Integer inboundTotal = 0;
        private Integer inboundSuccess = 0;

        private Integer outboundTotal = 0;
        private Integer outboundSuccess = 0;

        private String customNumber;
    }
}
