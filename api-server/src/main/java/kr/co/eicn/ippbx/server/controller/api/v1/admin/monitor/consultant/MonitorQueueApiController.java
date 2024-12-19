package kr.co.eicn.ippbx.server.controller.api.v1.admin.monitor.consultant;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable;
import kr.co.eicn.ippbx.model.entity.eicn.CurrentEICNCdrEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserInboundEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserOutboundEntity;
import kr.co.eicn.ippbx.model.enums.LicenseListType;
import kr.co.eicn.ippbx.model.enums.PersonSort;
import kr.co.eicn.ippbx.model.enums.PhoneInfoStatus;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.MemberStatusOfHunt;
import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.StatInboundService;
import kr.co.eicn.ippbx.server.service.StatOutboundService;
import kr.co.eicn.ippbx.server.service.StatUserInboundService;
import kr.co.eicn.ippbx.server.service.StatUserOutboundService;
import kr.co.eicn.ippbx.util.EicnUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.types.UInteger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 모니터링 > 상담원 모니터링 > 모니터링[헌트별]
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/monitor/consultant/queue", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitorQueueApiController extends ApiBaseController {

    private final StatInboundService statInboundService;
    private final StatUserInboundService statUserInboundService;
    private final StatUserOutboundService statUserOutboundService;
    private final PersonListRepository personListRepository;
    private final QueueMemberTableRepository queueMemberRepository;
    private final QueueNameRepository queueNameRepository;
    private final CmpMemberStatusCodeRepository cmpMemberStatusCodeRepository;
    private final CurrentMemberStatusRepository currentMemberStatusRepository;
    private final PhoneInfoRepository phoneInfoRepository;
    private final CurrentEICNCdrRepository currentEICNCdrRepository;

    /**
     * 요약 보기
     */
    @GetMapping("summary")
    public ResponseEntity<JsonResult<List<MonitorQueueSummaryResponse>>> MonitorQueueSummary() {
        Map<String, List<MemberStatusOfHunt>> status = queueMemberRepository.getStatusCountOfHunt().stream().collect(Collectors.groupingBy(MemberStatusOfHunt::getName));
        List<CmpMemberStatusCodeEntity> statusCodeList = cmpMemberStatusCodeRepository.findAll();
        List<QueueMemberLoginCountResponse> isLoginByQueueName = queueMemberRepository.getIsLoginByQueueName();

        List<MonitorQueueSummaryResponse> summaryResponseList = queueNameRepository.findAll().stream()
                .map((e) -> {
                    final MonitorQueueSummaryResponse response = convertDto(e, MonitorQueueSummaryResponse.class);

                    Map<Integer, Integer> statusCountMap = new LinkedHashMap<>();
                    if (Objects.nonNull(status.get(e.getName()))) {
                        for (MemberStatusOfHunt memberStatusOfHunt : status.get(e.getName())) {
                            statusCodeList.forEach(code -> statusCountMap.put(code.getStatusNumber(), 0));
                            statusCountMap.put(memberStatusOfHunt.getPaused(), memberStatusOfHunt.getCount());
                        }
                        response.setStatusToUserCount(statusCountMap);
                    }
                    response.setQueue(convertDto(e, SummaryQueueResponse.class));
                    response.setLoginUser(
                            isLoginByQueueName.stream().filter(queueCount -> queueCount.getIsLogin().equals("Y") && queueCount.getQueueName().equals(e.getName()))
                                    .map(QueueMemberLoginCountResponse::getCount).findFirst().orElse(0)
                    );
                    response.setLogoutUser(
                            isLoginByQueueName.stream().filter(queueCount -> queueCount.getIsLogin().equals("N") && queueCount.getQueueName().equals(e.getName()))
                                    .map(QueueMemberLoginCountResponse::getCount).findFirst().orElse(0)
                    );
                    response.setTotalUser(response.getLoginUser() + response.getLogoutUser());

                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(summaryResponseList));
    }

    /**
     * 상담원 상태
     */
    @GetMapping("person-status")
    public ResponseEntity<JsonResult<List<MonitorQueueSummaryPerson>>> MonitorQueueStatus() {
        final List<QueueName> queueNameList = queueNameRepository.findAll();
        final Map<String, PersonList> personListMap = personListRepository.findAll().stream().filter(e -> !e.getPeer().equals("") && e.getLicenseList().indexOf(LicenseListType.STAT.getCode()) > 0).collect(Collectors.toMap(PersonList::getPeer, e -> e));

        List<MonitorQueueSummaryPerson> summaryResponseList = queueMemberRepository.findAllByQueueName(queueNameList, Bool.N.getValue()).stream()
                .filter(e -> Objects.nonNull(personListMap.get(e.getMembername())))
                .map(e -> {
                    final MonitorQueueSummaryPerson response = convertDto(e, MonitorQueueSummaryPerson.class);

                    response.setPersonList(
                            personListRepository.findAll(PersonSort.NAME).stream()
                                    .filter(p -> p.getPeer().equals(e.getMembername()))
                                    .map(p -> convertDto(p, PersonListSummary.class))
                                    .collect(Collectors.toList())
                    );
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(summaryResponseList));
    }

    /**
     * 통계요약, 주요현황
     */
    @GetMapping("stat-summary")
    public ResponseEntity<JsonResult<MonitorQueueTotalResponse>> MonitorQueueStat() {
        final MonitorQueueTotalResponse statResponse = new MonitorQueueTotalResponse();
        final MonitorMajorStatusResponse majorResponse = new MonitorMajorStatusResponse();
        Map<String, List<MemberStatusOfHunt>> status = queueMemberRepository.getStatusCountOfHunt().stream().collect(Collectors.groupingBy(MemberStatusOfHunt::getName));
        Map<String, MonitorMajorStatusResponse> huntStatMap = statInboundService.getRepository().findAllByHunt();
        List<QueueMemberLoginCountResponse> isLoginByQueueName = queueMemberRepository.getIsLoginByQueueName();

        statResponse.setStatList(
                queueNameRepository.findAll().stream()
                .map((e) -> {
                    final MonitorQueueStatResponse response = convertDto(e, MonitorQueueStatResponse.class);
                    final DashCurrentResultCallResponse callResponse = currentMemberStatusRepository.getCurrentResultCall();

                    response.setQueueName(e.getName());
                    response.setQueueHanName(e.getHanName());
                    response.setInboundCallCnt(callResponse.getInCallingCount());
                    response.setOutboundCallCnt(callResponse.getOutCallingCount());

                    if (Objects.nonNull(status.get(e.getName()))) {
                        for (MemberStatusOfHunt memberStatusOfHunt : status.get(e.getName())) {
                            if (memberStatusOfHunt.getPaused() == 0)
                                response.setCounselorWaitCnt(memberStatusOfHunt.getCount());
                            else if (memberStatusOfHunt.getPaused() == 2)
                                response.setPostprocessStatusCnt(memberStatusOfHunt.getCount());
                            else
                                response.setEtcCnt(memberStatusOfHunt.getCount());
                        }
                    }

                    final MonitorMajorStatusResponse statusResponse = huntStatMap.get(e.getNumber());
                    response.setConnReq(statusResponse.getConnreq());
                    response.setSuccess(statusResponse.getSuccess());
                    response.setCallback(statusResponse.getCallbackSuccess());
                    majorResponse.setTotal(majorResponse.getTotal() + statusResponse.getTotal());
                    majorResponse.setSuccess(majorResponse.getSuccess() + statusResponse.getSuccess());
                    majorResponse.setCallback(majorResponse.getCallback() + statusResponse.getCallback());
                    majorResponse.setCallbackSuccess(majorResponse.getCallbackSuccess() + statusResponse.getCallbackSuccess());
                    final Integer loginCount = isLoginByQueueName.stream().filter(queueCount -> queueCount.getIsLogin().equals("Y") && queueCount.getQueueName().equals(e.getName()))
                            .map(QueueMemberLoginCountResponse::getCount).findFirst().orElse(0);

                    majorResponse.setLoginUserCnt(majorResponse.getLoginUserCnt() + loginCount);
                    majorResponse.setWaitPersonCnt(majorResponse.getWaitPersonCnt() + response.getCounselorWaitCnt());
                    majorResponse.setWorkingPersonCnt(majorResponse.getWorkingPersonCnt() + response.getWorkingPersonCount());
                    response.setServiceCounselorCnt(response.getWorkingPersonCount());
                    response.setResponseRate(Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(response.getSuccess(), response.getConnReq()))));

                    return response;
                })
                .collect(Collectors.toList()));
        statResponse.setResponseRate(majorResponse.getSuccess(), majorResponse.getTotal());
        statResponse.setCallbackProcessingRate(majorResponse.getCallbackSuccess(), majorResponse.getCallback());
        statResponse.setCallCounselRate(majorResponse.getLoginUserCnt(), majorResponse.getWorkingPersonCnt());
        statResponse.setCounselorStatus(majorResponse.getWaitPersonCnt(), majorResponse.getWorkingPersonCnt());

        return ResponseEntity.ok(data(statResponse));
    }

    @GetMapping("individual-stat")
    public ResponseEntity<JsonResult<List<MonitorQueuePersonStatResponse>>> getIndividualStat() {
        final List<MonitorQueuePersonStatResponse> rows = new ArrayList<>();
        final Map<String, StatUserInboundEntity> individualInboundStat = statUserInboundService.getRepository().findAllUserIndividualStat();
        final Map<String, StatUserOutboundEntity> individualOutboundStat = statUserOutboundService.getRepository().findAllUserIndividualStat();
        final Map<String, String> phoneInfoMap = phoneInfoRepository.findAllPhoneStatus();
        final Map<String, String> queueNameMap = queueNameRepository.getHanNameByQueueNumber();
        final List<PersonList> personList = personListRepository.findAll().stream().filter(e -> StringUtils.isNotEmpty(e.getPeer())).collect(Collectors.toList());
        final Map<String, QueueMemberTable> queueMemberMap = queueMemberRepository.findAllQueueMember();
        final Map<String, CurrentEICNCdrEntity> currentCdrByPeer = currentEICNCdrRepository.findAllCurrentCdrByPeer();



        for (PersonList personData : personList) {
            final PersonListSummary person = convertDto(personData, PersonListSummary.class);
            if (Objects.nonNull(queueMemberMap.get(person.getPeer()))) {
                QueueMemberTable queueMemberTable = queueMemberMap.get(person.getPeer());

                person.setPaused(queueMemberTable.getPaused());
                person.setIsLogin(queueMemberTable.getIsLogin());

                Map<UInteger, QueueMemberTable> queueMemberMap2 = queueMemberRepository.findAllQueueName(person.getId());
                for(UInteger key : queueMemberMap2.keySet()){
                    final MonitorQueuePersonStatResponse row = convertDto(queueMemberTable, MonitorQueuePersonStatResponse.class);
                    row.setPerson(person);
                    row.setIsPhone(PhoneInfoStatus.REGISTERED.getCode().equals(phoneInfoMap.get(person.getPeer())) ? "Y" : "N");
                    row.setQueueName(queueMemberMap2.get(key).getQueueName());
                    row.setQueueHanName(queueNameMap.get(queueMemberMap2.get(key).getQueueNumber()));

                    if (currentCdrByPeer.containsKey(person.getPeer())) {
                        CurrentEICNCdrEntity cdr = currentCdrByPeer.get(person.getPeer());

                        row.setInOut(cdr.getInOut());
                        row.setCustomNumber("O".equals(cdr.getInOut()) ? cdr.getDst() : cdr.getSrc());
                        row.setCurrentQueueHanName("O".equals(cdr.getInOut()) ? "발신" : queueNameMap.get(cdr.getSecondNum()));
                    }

                    final StatUserInboundEntity inboundStat = individualInboundStat.get(person.getId());
                    final StatUserOutboundEntity outboundStat = individualOutboundStat.get(person.getId());

                    row.setInboundSuccess(inboundStat != null ? inboundStat.getInSuccess() : 0);
                    row.setOutboundSuccess(outboundStat != null ? outboundStat.getOutSuccess() : 0);
                    row.setBillSecSum((inboundStat != null ? inboundStat.getInBillsecSum() : 0) + (outboundStat != null ? outboundStat.getOutBillsecSum() : 0));
                    row.setTotalStat();
                    row.setBillSecondAverage();

                    rows.add(row);

                }

            }
        }

        return ResponseEntity.ok(data(rows));
    }
}
