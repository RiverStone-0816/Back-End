package kr.co.eicn.ippbx.front.controller.web.admin.monitor;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ChattingApiInterface;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.PartMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.QueueMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.display.ScreenDataApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.dto.eicn.MonitorQueuePersonStatResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.entity.eicn.OrganizationMetaChatt;
import kr.co.eicn.ippbx.model.search.ChattingMemberSearchRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.*;
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

    private Map<String, String> getHavingPersonGroups(List<OrganizationMetaChatt> organizations) {
        final Map<String, String> groups = new HashMap<>();

        for (OrganizationMetaChatt e : organizations) {
            if (!e.getPersonList().isEmpty())
                groups.put(e.getGroupCode(), e.getGroupName());

            groups.putAll(getHavingPersonGroups(e.getOrganizationMetaChatt()));
        }

        return groups;
    }

    private Map<String, List<MonitorQueuePersonStatResponse>> getGroupToPersons(List<OrganizationMetaChatt> organizations, List<MonitorQueuePersonStatResponse> persons) {
        final Map<String, List<MonitorQueuePersonStatResponse>> groups = new HashMap<>();

        for (OrganizationMetaChatt e : organizations) {
            if (!e.getPersonList().isEmpty()) {
                final List<MonitorQueuePersonStatResponse> groupPersons = groups.getOrDefault(e.getGroupCode(), new ArrayList<>());
                for (PersonDetailResponse p1 : e.getPersonList())
                    persons.stream().filter(p -> Objects.equals(p.getPerson().getId(), p1.getId())).forEach(groupPersons::add);

                groups.put(e.getGroupCode(), groupPersons);
            }

            groups.putAll(getGroupToPersons(e.getOrganizationMetaChatt(), persons));
        }

        return groups;
    }

    @GetMapping("part")
    public String part(Model model) throws IOException, ResultFailException {
        final List<MonitorQueuePersonStatResponse> personStatuses = partMonitoringApiInterface.getIndividualStat();
        List<OrganizationMetaChatt> organization = chattingApiInterface.getOrganizationMetaChatt(new ChattingMemberSearchRequest());

        final Map<String, String> groups = getHavingPersonGroups(organization);
        model.addAttribute("groups", groups);

        final Map<String, List<MonitorQueuePersonStatResponse>> groupToPersons = getGroupToPersons(organization, personStatuses);
        model.addAttribute("groupToPersons", groupToPersons);

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("memberStatuses", memberStatuses);

        return "admin/monitor/consultant-status/part/ground";
    }

    @GetMapping("queue")
    public String queue(Model model) throws IOException, ResultFailException {
        final List<MonitorQueuePersonStatResponse> personStatuses = partMonitoringApiInterface.getIndividualStat();
        final Map<String, String> queues = personStatuses.stream().filter(e -> StringUtils.isNotEmpty(e.getQueueName())).collect(Collectors.toMap(MonitorQueuePersonStatResponse::getQueueName, MonitorQueuePersonStatResponse::getQueueHanName));
        model.addAttribute("queues", queues);

        final Map<String, List<MonitorQueuePersonStatResponse>> queueToPersons = new HashMap<>();
        model.addAttribute("queueToPersons", queueToPersons);

        personStatuses.stream().filter(e -> StringUtils.isNotEmpty(e.getQueueName())).forEach(e -> {
            final List<MonitorQueuePersonStatResponse> list = queueToPersons.getOrDefault(e.getQueueName(), new ArrayList<>());
            list.add(e);
            queueToPersons.put(e.getQueueName(), list);
        });

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("memberStatuses", memberStatuses);

        return "admin/monitor/consultant-status/queue/ground";
    }
}
