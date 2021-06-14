package kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.user;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.statdb.StatMemberStatusResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserInboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserOutboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserResponse;
import kr.co.eicn.ippbx.model.dto.util.*;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.Organization;
import kr.co.eicn.ippbx.model.entity.statdb.StatMemberStatusEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserInboundEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserOutboundEntity;
import kr.co.eicn.ippbx.model.enums.PersonPausedStatus;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.CmpMemberStatusCodeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.StatMemberStatusService;
import kr.co.eicn.ippbx.server.service.StatUserInboundService;
import kr.co.eicn.ippbx.server.service.StatUserOutboundService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.SearchCycleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 통계관리 > 상담원실적통계
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stat/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatUserApiController extends ApiBaseController {
    private final StatUserInboundService statUserInboundService;
    private final StatUserOutboundService statUserOutboundService;
    private final StatMemberStatusService StatMemberStatusService;
    private final PersonListRepository personListRepository;
    private final CmpMemberStatusCodeRepository cmpMemberStatusCodeRepository;
    private final CompanyTreeRepository companyTreeRepository;

    @GetMapping(value = "")
    public ResponseEntity<JsonResult<List<StatUserResponse<?>>>> list(StatUserSearchRequest search) {
        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
        if ((search.getEndDate().getTime() - search.getStartDate().getTime()) / 1000 > 6 * 30 * 24 * 60 * 60)
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.indays", "180일"));

        List<StatUserResponse<?>> rows = new ArrayList<>();

        List<StatUserInboundEntity> userInboundList = statUserInboundService.getRepository().findAllUserStat(search);
        List<StatUserOutboundEntity> userOutboundList = statUserOutboundService.getRepository().findAll(search);
        List<StatMemberStatusEntity> memberStatusList = StatMemberStatusService.getRepository().findAll(search);
        List<PersonList> personList = personListRepository.findAllByServiceHunt(search);
        List<CmpMemberStatusCodeEntity> statusCodeList = cmpMemberStatusCodeRepository.findAll();
        List<Organization> allOrganization = companyTreeRepository.getAllOrganization();

        List<?> dateByTypeList = SearchCycleUtils.getDateByType(search.getStartDate(), search.getEndDate(), search.getTimeUnit());

        for (Object timeInformation : dateByTypeList) {
            StatUserResponse<?> response = null;
            if (search.getTimeUnit().equals(SearchCycle.DATE)) {
                response = new StatUserResponse<>((DateResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.HOUR)) {
                response = new StatUserResponse<>((HourResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.WEEK)) {
                response = new StatUserResponse<>((WeekResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.MONTH)) {
                response = new StatUserResponse<>((MonthResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.DAY_OF_WEEK)) {
                response = new StatUserResponse<>((DayOfWeekResponse) timeInformation);
            }

            List<StatUserInboundEntity> statUserInboundList = SearchCycleUtils.streamFiltering(userInboundList, search.getTimeUnit(), timeInformation);
            List<StatUserOutboundEntity> statUserOutboundList = SearchCycleUtils.streamFiltering(userOutboundList, search.getTimeUnit(), timeInformation);
            List<StatMemberStatusEntity> statMemberStatusList = SearchCycleUtils.streamFiltering(memberStatusList, search.getTimeUnit(), timeInformation);

            List<StatUserResponse.UserStat> userStatList = new ArrayList<>();
            for (PersonList person : personList) {
                StatUserResponse.UserStat row = new StatUserResponse.UserStat();
                row.setIdName(person.getIdName());
                row.setGroupName(
                        allOrganization.stream().filter(organization -> organization.getGroupCode().equals(person.getGroupCode()))
                                .map(CompanyTree::getGroupName).findFirst().orElse("")
                );
                row.setGroupCode(person.getGroupCode());
                row.setGroupTreeName(person.getGroupTreeName());

                Stream<StatUserInboundEntity> userInboundStream = statUserInboundList.stream().filter(inbound -> inbound.getGroupCode().equals(person.getGroupCode()) && inbound.getUserid().equals(person.getId()));
                Stream<StatUserOutboundEntity> userOutboundStream = statUserOutboundList.stream().filter(outbound -> outbound.getGroupCode().equals(person.getGroupCode()) && outbound.getUserid().equals(person.getId()));
                Stream<StatMemberStatusEntity> memberStream = statMemberStatusList.stream().filter(member -> member.getUserid().equals(person.getId()));

                row.setInboundStat(
                        userInboundStream.map(inbound -> convertDto(inbound, StatUserInboundResponse.class))
                                .findFirst().orElse(new StatUserInboundResponse())
                );

                row.setOutboundStat(
                        userOutboundStream.map(outbound -> convertDto(outbound, StatUserOutboundResponse.class))
                                .findFirst().orElse(new StatUserOutboundResponse())
                );

                StatMemberStatusResponse memberStatus = new StatMemberStatusResponse();
                Map<Integer, Long> statusCountMap = new LinkedHashMap<>();
                statusCodeList.forEach(code -> statusCountMap.put(code.getStatusNumber(), 0L));
                memberStream.forEach(i -> {
                    if (i.getStatus() == 2) {
                        memberStatus.setPostProcess(i.getTotal());
                        memberStatus.setPostProcessTime(i.getDiffSum());
                    } else {
                        statusCountMap.put(i.getStatus(), i.getDiffSum());
                    }
                });

                memberStatus.setStatusCountMap(statusCountMap);

                row.setMemberStatusStat(memberStatus);
                row.setTotalCnt();
                row.setTotalBillSec();
                userStatList.add(row);
            }

            if (response != null) {
                response.setUserStatList(userStatList);
                rows.add(response);
            }
        }

        return ResponseEntity.ok(data(rows));
    }

    @GetMapping("total")
    public ResponseEntity<JsonResult<StatUserResponse.UserStat>> getTotal(StatUserSearchRequest search) {
        search.setTimeUnit(null);
        final StatUserInboundResponse inboundTotal = statUserInboundService.getRepository().findAllUserTotalStat(search).stream().findFirst().map(e -> convertDto(e, StatUserInboundResponse.class)).orElse(new StatUserInboundResponse());
        final StatUserOutboundResponse outboundTotal = statUserOutboundService.getRepository().findAllTotal(search).stream().findFirst().map(e -> convertDto(e, StatUserOutboundResponse.class)).orElse(new StatUserOutboundResponse());
        Map<Integer, Long> statusCountMap = new LinkedHashMap<>();
        final StatMemberStatusResponse memberTotal = new StatMemberStatusResponse();
        StatMemberStatusService.getRepository().findAllTotal(search)
                .forEach(e -> {
                    if (e.getStatus().equals(PersonPausedStatus.AFTER_TREATMENT.getCode())) {
                        memberTotal.setPostProcess(e.getTotal());
                        memberTotal.setPostProcessTime(e.getDiffSum());
                    } else {
                        statusCountMap.put(e.getStatus(), e.getDiffSum());
                    }
                });
        memberTotal.setStatusCountMap(statusCountMap);

        final StatUserResponse.UserStat result = new StatUserResponse.UserStat();

        result.setOutboundStat(outboundTotal);
        result.setInboundStat(inboundTotal);
        result.setMemberStatusStat(memberTotal);
        result.setTotalCnt();
        result.setTotalBillSec();

        return ResponseEntity.ok(data(result));
    }
}
