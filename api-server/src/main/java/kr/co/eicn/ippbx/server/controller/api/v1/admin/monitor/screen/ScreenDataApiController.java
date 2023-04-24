package kr.co.eicn.ippbx.server.controller.api.v1.admin.monitor.screen;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.DashServiceStatResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.form.ByHuntSuccessPerData;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.CmpMemberStatusCodeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueMemberTableRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueNameRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.server.service.StatInboundService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.QueueName.QUEUE_NAME;

/**
 * 모니터링 > 전광판 > 통화전광판
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/monitor/screen-data", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScreenDataApiController extends ApiBaseController {

    private final QueueNameRepository queueNameRepository;
    private final QueueMemberTableRepository queueMemberTableRepository;
    private final CmpMemberStatusCodeRepository cmpMemberStatusCodeRepository;
    private final ServiceRepository serviceRepository;
    private final StatInboundService statInboundService;

    @GetMapping(value = "", params = "expressionType=INTEGRATION")
    public JsonResult<IntegrationData> integration() {
        final List<CmpMemberStatusCodeEntity> memberStatusCodes = cmpMemberStatusCodeRepository.findAll();
        final List<QueueNameRepository.QueueMemberStatus> pauseMemberStatues = queueMemberTableRepository.memberStatusesByPause();
        DashServiceStatResponse dashServiceStat = statInboundService.getRepository().getDashServiceStat("", "");

        final IntegrationData result = new IntegrationData();

        memberStatusCodes.forEach(code -> {
            result.getConstantStatusCounts().put(code.getStatusNumber(), 0);
            for (QueueNameRepository.QueueMemberStatus memberStatus : pauseMemberStatues) {
                if (code.getStatusNumber().equals(memberStatus.getPaused())) {
                    if (code.getStatusNumber() != 9 && "Y".equals(memberStatus.getIsLogin()))
                        result.getConstantStatusCounts().put(code.getStatusNumber(), memberStatus.getCnt());
                    if (code.getStatusNumber() == 9 && "N".equals(memberStatus.getIsLogin()))
                        result.getConstantStatusCounts().put(code.getStatusNumber(), memberStatus.getCnt());
                }
            }
        });

        result.setInboundCall(dashServiceStat.getTotalCnt());
        result.setConnectionRequest(dashServiceStat.getConnReqCnt());
        result.setSuccessCall(dashServiceStat.getSuccessCnt());
        result.setCancelCall(dashServiceStat.getCancelCnt());
        result.setResponseRate((double) dashServiceStat.getRateValue());

        // 기타
        result.getConstantStatusCounts().put(99, pauseMemberStatues.stream().filter(e -> memberStatusCodes.stream().noneMatch(code -> code.getStatusNumber().equals(e.getPaused())))
                .mapToInt(QueueNameRepository.QueueMemberStatus::getCnt).sum()
        );

        return JsonResult.data(result);
    }

    @GetMapping(value = "", params = "expressionType=BY_HUNT_SUCCESSPER")
    public JsonResult<ByHuntSuccessPerData> byHuntSuccessPer() {
        final Map<?, BigDecimal> response = statInboundService.getRepository().getSuccessPer();
        final Map<String, String> hunt = queueNameRepository.findAll().stream().collect(Collectors.toMap(QueueName::getNumber, QueueName::getName));
        final List<ByHuntSuccessPerData.ByHuntSuccessPer> responses = new ArrayList<>();
        final ByHuntSuccessPerData byHuntSuccessPerData = new ByHuntSuccessPerData();
        for (Object key : response.keySet()) {
            ByHuntSuccessPerData.ByHuntSuccessPer e = new ByHuntSuccessPerData.ByHuntSuccessPer();
            e.setQueueNumber((String) key);
            e.setSuccessPer(response.get(key).doubleValue());
            e.setQueueName(hunt.get(key));
            responses.add(e);
        }
        byHuntSuccessPerData.setByHuntSuccessPerList(responses);
        return JsonResult.data(byHuntSuccessPerData);
    }

    @GetMapping(value = "", params = "expressionType=BY_HUNT")
    public JsonResult<ByHuntData> byHunt() {
        final List<CmpMemberStatusCodeEntity> memberStatusCodes = cmpMemberStatusCodeRepository.findAll();
        final List<QueueName> queues = queueNameRepository.findAll(QUEUE_NAME.NAME.like("QUEUE%")).stream().sorted(Comparator.comparing(QueueName::getHanName)).collect(Collectors.toList());
        final Map<String, StatInboundEntity> statInboundEntityMap = statInboundService.getRepository().groupingByServices().stream().collect(Collectors.toMap(StatInboundEntity::getServiceNumber, e -> e));
        final List<QueueNameRepository.QueueMemberStatus> queueMemberStatuses = queueNameRepository.memberStatusByQueues();
        final List<ServiceList> serviceList = serviceRepository.findAll();

        final ByHuntData result = new ByHuntData();

        result.setHunts(
                queues.stream().map(e -> {
                    final ByHuntData.HuntData huntData = new ByHuntData.HuntData();
                    huntData.setQueueName(e.getName());
                    huntData.setQueueKoreanName(e.getHanName());
                    huntData.setQueueServiceNumber(e.getSvcNumber());

                    memberStatusCodes.forEach(code -> {
                        huntData.getConstantStatusCounts().put(code.getStatusNumber(), 0);
                        for (QueueNameRepository.QueueMemberStatus memberStatus : queueMemberStatuses) {
                            if (e.getNumber().equals(memberStatus.getNumber())) {
                                if (code.getStatusNumber().equals(memberStatus.getPaused())) {
                                    if (code.getStatusNumber() != 9 && "Y".equals(memberStatus.getIsLogin()))
                                        huntData.getConstantStatusCounts().put(code.getStatusNumber(), memberStatus.getCnt());
                                    if (code.getStatusNumber() == 9 && "N".equals(memberStatus.getIsLogin()))
                                        huntData.getConstantStatusCounts().put(code.getStatusNumber(), memberStatus.getCnt());
                                }
                            }
                        }
                    });

                    return huntData;
                }).collect(Collectors.toList())
        );

        result.setServices(
                serviceList.stream().map(service -> {
                    final ByHuntData.ServiceData serviceData = new ByHuntData.ServiceData();
                    serviceData.setServiceName(service.getSvcName());
                    if (statInboundEntityMap.get(service.getSvcNumber()) != null) {
                        final StatInboundEntity entity = statInboundEntityMap.get(service.getSvcNumber());
                        serviceData.setCustomerWaiting(0);
                        serviceData.setOnlyReadCall(entity.getOnlyread());
                        serviceData.setConnectionRequest(entity.getConnreq());
                        serviceData.setSuccessCall(entity.getSuccess());
                        serviceData.setCancelCall(entity.getCancel());
                        serviceData.setTotalCall(entity.getTotal());
                        serviceData.setResponseRate(entity.getResponseRate());
                    }

                    return serviceData;
                }).collect(Collectors.toList())
        );

        return JsonResult.data(result);
    }

    @GetMapping(value = "", params = "expressionType=BY_SERVICE")
    public JsonResult<ByServiceData> byService() {
        final List<CmpMemberStatusCodeEntity> memberStatusCodes = cmpMemberStatusCodeRepository.findAll();
        final List<ServiceList> services = serviceRepository.findAll().stream()
                .sorted(Comparator.comparing(ServiceList::getSvcName))
                .collect(Collectors.toList());
        final Map<String, StatInboundEntity> statInboundEntityMap = statInboundService.getRepository().groupingByServices().stream().collect(Collectors.toMap(StatInboundEntity::getServiceNumber, e -> e));
        final List<QueueNameRepository.QueueMemberStatus> serviceMemberStatuses = queueNameRepository.memberStatusByServices();

        final ByServiceData result = new ByServiceData();

        result.setServices(
                services.stream().map(e -> {
                    final ScreenDataApiController.IntegrationData inData = new ScreenDataApiController.IntegrationData();
                    inData.setServiceName(e.getSvcName());
                    inData.setServiceNumber(e.getSvcNumber());

                    if (statInboundEntityMap.get(e.getSvcNumber()) != null) {
                        final StatInboundEntity entity = statInboundEntityMap.get(e.getSvcNumber());
                        inData.setCustomerWaiting(0);
                        inData.setInboundCall(entity.getTotal());
                        inData.setConnectionRequest(entity.getConnreq());
                        inData.setSuccessCall(entity.getSuccess());
                        inData.setCancelCall(entity.getCancel());
                        inData.setResponseRate(entity.getResponseRate());
                    }

                    memberStatusCodes.forEach(code -> {
                        inData.getConstantStatusCounts().put(code.getStatusNumber(), 0);
                        for (QueueNameRepository.QueueMemberStatus memberStatus : serviceMemberStatuses) {
                            if (e.getSvcNumber().equals(memberStatus.getSvcNumber())) {
                                if (code.getStatusNumber().equals(memberStatus.getPaused())) {
                                    if (code.getStatusNumber() != 9 && "Y".equals(memberStatus.getIsLogin()))
                                        inData.getConstantStatusCounts().put(code.getStatusNumber(), memberStatus.getCnt());
                                    if (code.getStatusNumber() == 9 && "N".equals(memberStatus.getIsLogin()))
                                        inData.getConstantStatusCounts().put(code.getStatusNumber(), memberStatus.getCnt());
                                }
                            }
                        }
                    });

                    return inData;
                }).collect(Collectors.toList())
        );

        return JsonResult.data(result);
    }


    @Data
    public static class IntegrationData {
        private String serviceName;
        private String serviceNumber;
        private Map<Integer, Integer> constantStatusCounts = new LinkedHashMap<>();

        private Integer customerWaiting = 0; // 대기고객수
        private Integer inboundCall = 0;
        private Integer connectionRequest = 0; // 연결요청
        private Integer successCall = 0; // 응답호
        private Integer cancelCall = 0; // 포기호
        private Double responseRate = 0.0; // 응답률
    }

    @Data
    public static class ByHuntData {
        private String representativeServiceName;
        private String representativeServiceNumber;
        private List<HuntData> hunts = new ArrayList<>();
        private List<ServiceData> services = new ArrayList<>();

        @Data
        public static class HuntData {
            private String queueName;
            private String queueKoreanName;
            private String queueServiceNumber;
            private Map<Integer, Integer> constantStatusCounts = new LinkedHashMap<>();
            private Integer customerWaiting = 0; // 대기고객수
        }

        @Data
        public static class ServiceData {
            private String serviceName;
            private Integer customerWaiting = 0; // 대기고객수
            private Integer totalCall = 0; // 인입호수
            private Integer onlyReadCall = 0; // 단순조회
            private Integer connectionRequest = 0; // 연결요청
            private Integer successCall = 0; // 응답호
            private Integer cancelCall = 0; // 포기호
            private Double responseRate = 0.0; // 응답률
        }
    }

    @Data
    public static class ByServiceData {
        private List<IntegrationData> services = new ArrayList<>();
    }
}
