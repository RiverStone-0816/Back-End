package kr.co.eicn.ippbx.server.controller.api.v1.admin.monitor.consultant;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.controller.api.v1.admin.dashboard.DashboardApiController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueMemberTable;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatInbound;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.statdb.*;
import kr.co.eicn.ippbx.model.entity.eicn.CenterMemberStatusCountEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.MemberStatusOfHunt;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatOutboundEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserInboundEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserOutboundEntity;
import kr.co.eicn.ippbx.model.enums.PhoneInfoStatus;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.StatInboundService;
import kr.co.eicn.ippbx.server.service.StatOutboundService;
import kr.co.eicn.ippbx.server.service.StatUserInboundService;
import kr.co.eicn.ippbx.server.service.StatUserOutboundService;
import kr.co.eicn.ippbx.util.EicnUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 모니터링 > 상담원 모니터링 > 모니터링[부서별]
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/monitor/consultant/part", produces = MediaType.APPLICATION_JSON_VALUE)
public class MonitorPartApiController extends ApiBaseController {
    private final StatInboundService statInboundService;
    private final StatOutboundService statOutboundService;
    private final StatUserInboundService statUserInboundService;
    private final StatUserOutboundService statUserOutboundService;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final QueueNameRepository queueNameRepository;
    private final CmpMemberStatusCodeRepository cmpMemberStatusCodeRepository;
    private final CurrentMemberStatusRepository currentMemberStatusRepository;
    private final DashboardApiController dashboardApiController;
    private final PhoneInfoRepository phoneInfoRepository;
    private final PersonListRepository personListRepository;
    private final CallbackRepository callbackRepository;

    /**
     * 센터현황별 모니터링
     */
    @GetMapping("center")
    public ResponseEntity<JsonResult<CenterStatusResponse>> getCenterStat() {
        final CenterStatResponse centerMonitoring = statInboundService.getRepository().getCenterMonitoring();
        final CenterMemberStatusCountEntity centerStatusCount = queueMemberTableRepository.getCenterStatusCount();
        final CenterMemberStatusCountEntity callingCount = currentMemberStatusRepository.getCenterStatusCount();
        final Integer incompleteCallbackCount = callbackRepository.getTodayIncompleteCallbackCount();

        final CenterStatusResponse centerStatusResponse = new CenterStatusResponse();
        centerStatusResponse.setRateValue(EicnUtils.getRateValue(centerMonitoring.getSuccess(), centerMonitoring.getConnreq()));
        centerStatusResponse.setWaitPersonCount(centerStatusCount.getWaitCount());
        centerStatusResponse.setWorkingPerson(centerStatusCount.getWorkingPerson());
        centerStatusResponse.setTotalCallback(centerMonitoring.getCallbackSuccess());
        centerStatusResponse.setProcessedCallback(centerMonitoring.getCallbackSuccess() - incompleteCallbackCount);
        centerStatusResponse.setUnprocessedCallback(incompleteCallbackCount);
        centerStatusResponse.setInboundCall(callingCount.getInCallingCount());
        centerStatusResponse.setOutboundCall(callingCount.getOutCallingCount());
        centerStatusResponse.setPostProcess(centerStatusCount.getPostProcessCount());
        centerStatusResponse.setEtc(centerStatusCount.getEtcCount());
        centerStatusResponse.setLoginCount(centerStatusCount.getLoginCount());
        centerStatusResponse.setLogoutCount(centerStatusCount.getLogoutCount());

        return ResponseEntity.ok(data(centerStatusResponse));
    }

    /**
     * 시간별 최대 대기자수
     */
    @GetMapping("hour")
    public ResponseEntity<JsonResult<Map<Byte, Integer>>> getMaximumNumberOfWaitCountByTime() {
        Map<Byte, Integer> waitCountMap = statInboundService.getRepository().getMaximumNumberOfWaitCountByTime();

        for (byte hour = 0; hour < 24; hour++)
            waitCountMap.putIfAbsent(hour, 0);

        return ResponseEntity.ok(data(waitCountMap));
    }

    /**
     * 헌트모니터링
     **/
    @GetMapping("hunt-monitor")
    public ResponseEntity<JsonResult<List<HuntMonitorResponse>>> getHuntMonitor() {
        final List<HuntMonitorResponse> rows = new ArrayList<>();
        final List<QueueName> queueNameList = queueNameRepository.findAll();
        final Map<String, List<MemberStatusOfHunt>> queueMemberList = queueMemberTableRepository.getStatusCountOfHunt().stream().collect(Collectors.groupingBy(MemberStatusOfHunt::getName));
        final Map<String, String> queueNameMap = queueNameRepository.getHuntNameMap();
        final List<CmpMemberStatusCodeEntity> statusCodeList = cmpMemberStatusCodeRepository.findAll();
        final List<QueueMemberLoginCountResponse> isLoginByQueueName = queueMemberTableRepository.getIsLoginByQueueName();

        for (QueueName queueName : queueNameList) {
            Map<Integer, Integer> statusCountMap = new LinkedHashMap<>();
            statusCodeList.forEach(code -> statusCountMap.put(code.getStatusNumber(), 0));

            if (Objects.nonNull(queueMemberList.get(queueName.getName()))) {
                queueMemberList.forEach((k, v) -> {
                    if (queueName.getName().equals(k)) {
                        HuntMonitorResponse huntMonitor = new HuntMonitorResponse();
                        huntMonitor.setQueueName(k);
                        huntMonitor.setQueueHanName(queueNameMap.get(k));
                        for (MemberStatusOfHunt memberStatusOfHunt : v) {
                            statusCountMap.put(memberStatusOfHunt.getPaused(), memberStatusOfHunt.getCount());
                        }
                        huntMonitor.setStatusCountMap(statusCountMap);

                        huntMonitor.setLoginCount(
                                isLoginByQueueName.stream().filter(e -> e.getIsLogin().equals("Y") && e.getQueueName().equals(k))
                                        .map(QueueMemberLoginCountResponse::getCount).findFirst().orElse(0)
                        );
                        huntMonitor.setLogoutCount
                                (isLoginByQueueName.stream().filter(e -> e.getIsLogin().equals("N") && e.getQueueName().equals(k))
                                        .map(QueueMemberLoginCountResponse::getCount).findFirst().orElse(0)
                                );
                        huntMonitor.setTotal(huntMonitor.getLoginCount() + huntMonitor.getLogoutCount());
                        rows.add(huntMonitor);
                    }
                });
            } else {
                HuntMonitorResponse huntMonitor = new HuntMonitorResponse();
                huntMonitor.setQueueName(queueName.getName());
                huntMonitor.setQueueHanName(queueName.getHanName());
                statusCountMap.put(0, 0);
                huntMonitor.setStatusCountMap(statusCountMap);

                rows.add(huntMonitor);
            }
        }

        return ResponseEntity.ok(data(rows));
    }

    /**
     * 우수실적상담원
     */
    @GetMapping("excellent-consultant")
    public ResponseEntity<JsonResult<List<ExcellentConsultant>>> getExcellentCS() {
        return dashboardApiController.getExcellentCS();
    }

    /**
     * 통합통계모니터링
     */
    @GetMapping("total-stat")
    public ResponseEntity<JsonResult<TotalStatResponse>> getTotalStat() {
        TotalStatResponse totalStatResponse = statInboundService.getRepository().getTodayStat();
        StatOutboundEntity todayOutboundStat = statOutboundService.getRepository().getTodayStat();

        totalStatResponse.setOutboundSuccess(todayOutboundStat.getSuccess());
        totalStatResponse.setOutboundCancel(todayOutboundStat.getCancel());
        totalStatResponse.setRateValue();

        return ResponseEntity.ok(data(totalStatResponse));
    }

    /**
     * 통합통계 비교 그래프
     */
    @GetMapping("compared-hunt-total-call-count-by-time")
    public ResponseEntity<JsonResult<TotalStatGraphResponse>> getComparedHuntTotalCallCountByTime(@RequestParam String queueAName, @RequestParam String queueBName) {
        final TotalStatGraphResponse response = new TotalStatGraphResponse();
        final Map<String, List<StatInboundEntity>> todayStatGraphData = statInboundService.getRepository().getHourStatGraph(queueAName, queueBName).stream().collect(Collectors.groupingBy(StatInboundEntity::getHuntNumber));
        final Map<String, List<StatInboundEntity>> weeklyStatGraphData = statInboundService.getRepository().getWeeklyStatGraph(queueAName, queueBName).stream().collect(Collectors.groupingBy(CommonStatInbound::getHuntNumber));
        final List<QueueName> queueNameList = queueNameRepository.findAll().stream().filter(queueName -> queueName.getName().equals(queueAName) || queueName.getName().equals(queueBName)).collect(Collectors.toList());

        for (QueueName queueName : queueNameList) {
            final Map<Byte, Integer> statHourDataMap = new LinkedHashMap<>();
            for (byte i = 0; i < 24; i++)
                statHourDataMap.put(i, 0);
            if (todayStatGraphData.containsKey(queueName.getNumber()))
                for (StatInboundEntity data : todayStatGraphData.get(queueName.getNumber()))
                    statHourDataMap.put(data.getStatHour(), data.getSuccess());

            final Map<Byte, Integer> statWeekDataMap = new LinkedHashMap<>();
            for (byte i = 0; i < 24; i++)
                statWeekDataMap.put(i, 0);
            if (weeklyStatGraphData.containsKey(queueName.getNumber()))
                for (StatInboundEntity data : weeklyStatGraphData.get(queueName.getNumber()))
                    statWeekDataMap.put(data.getStatHour(), data.getSuccess());

            final StatHourGraphData hourGraphData = new StatHourGraphData();
            final StatWeekGraphData weekGraphData = new StatWeekGraphData();

            hourGraphData.setQueueName(queueName.getName());
            weekGraphData.setQueueName(queueName.getName());

            hourGraphData.setDataMap(statHourDataMap);
            weekGraphData.setDataMap(statWeekDataMap);

            if (queueName.getName().equals(queueAName)) {
                response.setHuntAHourData(hourGraphData);
                response.setHuntAWeekData(weekGraphData);
            } else if (queueName.getName().equals(queueBName)) {
                response.setHuntBHourData(hourGraphData);
                response.setHuntBWeekData(weekGraphData);
            }
        }

        return ResponseEntity.ok(data(response));
    }

    /**
     * 헌트별 통계 모니터링
     */
    @GetMapping("hunt-stat")
    public ResponseEntity<JsonResult<List<HuntStatMonitorResponse>>> getHuntStat() {
        final List<QueueName> queueNameList = queueNameRepository.findAll();
        final Map<String, MonitorMajorStatusResponse> huntStatMap = statInboundService.getRepository().findAllByHunt();
        List<HuntStatMonitorResponse> huntStatList = new ArrayList<>();

        queueNameList.forEach(queue -> {
            HuntStatMonitorResponse response = new HuntStatMonitorResponse();
            MonitorMajorStatusResponse data = huntStatMap.get(queue.getNumber());

            response.setHuntName(queue.getHanName());
            response.setTotal(data.getTotal());
            response.setConnectionRequest(data.getConnreq());
            response.setSuccess(data.getSuccess());
            response.setCancel(data.getCancel());
            response.setCallback(data.getCallbackSuccess());
            response.setResponseRate();

            huntStatList.add(response);
        });

        return ResponseEntity.ok(data(huntStatList));
    }

    /**
     * 헌트별 통계 그래프
     */
    @GetMapping("hunt-total-call-count-by-time")
    public ResponseEntity<JsonResult<List<StatHourGraphData>>> getHuntTotalCallCountByTime() {
        final List<StatHourGraphData> result = new ArrayList<>();
        final List<QueueName> queues = queueNameRepository.findAll();

        final Map<String, List<StatInboundEntity>> hourDataGroupingByQueueNumber = statInboundService.getRepository().getHourStatGraph().stream().collect(Collectors.groupingBy(StatInboundEntity::getHuntNumber));

        queues.forEach(queue -> {
            final Map<Byte, Integer> statHourDataMap = new LinkedHashMap<>();
            for (byte i = 0; i < 24; i++)
                statHourDataMap.put(i, 0);

            if (hourDataGroupingByQueueNumber.containsKey(queue.getNumber()))
                for (StatInboundEntity data : hourDataGroupingByQueueNumber.get(queue.getNumber()))
                    statHourDataMap.put(data.getStatHour(), data.getSuccess());

            final StatHourGraphData graphData = new StatHourGraphData();

            graphData.setQueueName(queue.getName());
            graphData.setDataMap(statHourDataMap);
            result.add(graphData);
        });

        return ResponseEntity.ok(data(result));
    }

    @GetMapping("individual-stat")
    public ResponseEntity<JsonResult<List<MonitorQueuePersonStatResponse>>> getIndividualStat() {
        final List<MonitorQueuePersonStatResponse> rows = new ArrayList<>();
        final Map<String, StatUserInboundEntity> individualInboundStat = statUserInboundService.getRepository().findAllUserIndividualStat();
        final Map<String, StatUserOutboundEntity> individualOutboundStat = statUserOutboundService.getRepository().findAllUserIndividualStat();
        final Map<String, String> phoneInfoMap = phoneInfoRepository.findAllPhoneStatus();
        final Map<String, String> queueNameMap = queueNameRepository.getHuntNameMap();
        final List<PersonList> personList = personListRepository.findAll().stream().filter(e -> StringUtils.isNotEmpty(e.getPeer())).collect(Collectors.toList());
        final Map<String, QueueMemberTable> queueMemberMap = queueMemberTableRepository.findAllQueueMember();
        final Map<String, QueueMemberTable> queueMemberMap2 = queueMemberTableRepository.findAllQueueName();



        for (PersonList personData : personList) {
            final PersonListSummary person = convertDto(personData, PersonListSummary.class);
            if (Objects.nonNull(queueMemberMap.get(person.getPeer()))) {
                QueueMemberTable queueMemberTable = queueMemberMap.get(person.getPeer());
                QueueMemberTable queueMemberTable2 = queueMemberMap2.get(person.getPeer());

                person.setPaused(queueMemberTable.getPaused());
                person.setIsLogin(queueMemberTable.getIsLogin());

                final MonitorQueuePersonStatResponse row = convertDto(queueMemberTable, MonitorQueuePersonStatResponse.class);

                row.setPerson(person);
                row.setIsPhone(PhoneInfoStatus.REGISTERED.getCode().equals(phoneInfoMap.get(person.getPeer())) ? "Y" : "N");
                row.setQueueName(queueMemberTable2.getQueueName());
                row.setQueueHanName(queueNameMap.get(queueMemberTable2.getQueueName()));

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

        return ResponseEntity.ok(data(rows));
    }
}
