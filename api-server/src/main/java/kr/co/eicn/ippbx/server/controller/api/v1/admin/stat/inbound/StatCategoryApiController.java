package kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.inbound;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatInbound;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryIvrResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryIvrTreeResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.ServiceNumberIvrRootEntity;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.search.StatCategorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.IvrTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ScheduleInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.server.service.StatCategoryService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.SearchCycleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 통계관리 > 인바운드통계 > 인입경로별통계
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stat/inbound/category", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatCategoryApiController extends ApiBaseController {
    private final StatCategoryService    statCategoryService;
    private final IvrTreeRepository      ivrTreeRepository;
    private final ServiceRepository      serviceRepository;
    private final ScheduleInfoRepository scheduleInfoRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<StatCategoryResponse<?>>>> list(StatCategorySearchRequest search) {
        final List<StatCategoryResponse<?>> responseList = new ArrayList<>();
        final List<IvrTree> ivrMultiList = ivrTreeRepository.findAllByRoot(search.getIvrMulti());
        final List<ServiceList> serviceList = serviceRepository.findAllBySvcNumber(search.getServiceNumbers());

        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");
        if ((search.getEndDate().getTime() - search.getStartDate().getTime()) / 1000 > 6 * 30 * 24 * 60 * 60)
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.indays", "180일"));

        final List<StatInboundEntity> inboundList = statCategoryService.getRepository().findAllCategoryStat(search);
        final Map<String, Set<String>> svcNumberIvrSchedule = scheduleInfoRepository.getServiceNumberIvrSchedule(search.getServiceNumbers()).stream()
                .collect(Collectors.groupingBy(ServiceNumberIvrRootEntity::getNumber, Collectors.mapping(ServiceNumberIvrRootEntity::getIvrRoot, Collectors.toSet())));
        statCategoryService.getRepository().getStatNumberIvrRoot(search)
                .forEach(e -> {
                    if (svcNumberIvrSchedule.containsKey(e.getNumber()))
                        svcNumberIvrSchedule.get(e.getNumber()).add(Integer.valueOf(e.getIvrRoot()).toString());
                });

        final Set<Integer> targetRootIvrSet = new HashSet<>();
        svcNumberIvrSchedule.values().forEach(e -> {
            final List<Integer> target = e.stream().map(Integer::valueOf).toList();
            targetRootIvrSet.addAll(target);
        });

        final Map<Integer, List<IvrTree>> iverTreeByRootMap = new HashMap<>();
        targetRootIvrSet.forEach(root -> iverTreeByRootMap.putIfAbsent(root, ivrTreeRepository.findAllByParentCode(root, root)));

        final List<?> dateByTypeList = SearchCycleUtils.getDateByType(search.getStartDate(), search.getEndDate(), search.getTimeUnit());
        for (Object timeInformation : dateByTypeList) {
            final Map<String, List<StatInboundEntity>> statInboundByService = SearchCycleUtils.streamFiltering(inboundList, search.getTimeUnit(), timeInformation).stream().collect(Collectors.groupingBy(CommonStatInbound::getServiceNumber));
            serviceList.forEach(service -> {
                final StatCategoryResponse<Object> response = new StatCategoryResponse<>();
                final List<StatInboundEntity> statInboundList = statInboundByService.containsKey(service.getSvcNumber()) ? statInboundByService.get(service.getSvcNumber()) : new ArrayList<>();
                response.setTimeInformation(timeInformation);
                response.setSvcName(service.getSvcName());

                response.setRecordList(
                        svcNumberIvrSchedule.containsKey(service.getSvcNumber())
                                ? ivrMultiList.stream().filter(e -> svcNumberIvrSchedule.get(service.getSvcNumber()).contains(e.getRoot().toString()))
                                .map(i -> {
                                    final StatCategoryIvrResponse ivrResponse = new StatCategoryIvrResponse();
                                    ivrResponse.setIvrName(i.getName());
                                    ivrResponse.setRecordNameList(statCategoryService.convertIvrPath(iverTreeByRootMap.get(i.getRoot()), statInboundList));

                                    return ivrResponse;
                                })
                                .collect(Collectors.toList())
                                : new ArrayList<>()
                );
                response.setMaxLevel(
                        response.getRecordList().stream()
                                .map(e -> e.getRecordNameList().stream().
                                        map(IvrTree::getLevel).max(Integer::compareTo).orElse(0))
                                .max(Integer::compareTo).orElse(0)
                );

                responseList.add(response);
            });
        }
        return ResponseEntity.ok(data(responseList));
    }

    /**
     * IVR 목록
     */
    @GetMapping("ivr-tree")
    public ResponseEntity<JsonResult<List<StatCategoryIvrTreeResponse>>> ivrTree() {
        return ResponseEntity.ok(data(
                ivrTreeRepository.findAll().stream()
                        .filter(e -> e.getLevel().equals(0) && e.getButton().isEmpty())
                        .map(e -> convertDto(e, StatCategoryIvrTreeResponse.class))
                        .collect(Collectors.toList())));
    }
}
