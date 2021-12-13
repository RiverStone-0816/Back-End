package kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.hunt;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.model.dto.statdb.StatHuntInboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatHuntResponse;
import kr.co.eicn.ippbx.model.dto.util.*;
import kr.co.eicn.ippbx.model.entity.statdb.StatUserInboundEntity;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatHuntSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyTreeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueNameRepository;
import kr.co.eicn.ippbx.server.service.StatUserInboundService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.SearchCycleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 통계관리 > 큐그룹별통계
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stat/hunt", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatHuntApiController extends ApiBaseController {
    private final StatUserInboundService statUserInboundService;
    private final CompanyTreeRepository companyTreeRepository;
    private final QueueNameRepository queueNameRepository;

    @GetMapping(value = "")
    public ResponseEntity<JsonResult<List<StatHuntResponse<?>>>> list(StatHuntSearchRequest search) {
        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");
        if ((search.getEndDate().getTime() - search.getStartDate().getTime()) / 1000 > 6 * 30 * 24 * 60 * 60)
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.indays", "180일"));

        final List<StatHuntResponse<?>> rows = new ArrayList<>();

        final Optional<CompanyTree> searchGroup = Optional.ofNullable(companyTreeRepository.findOneByGroupCode(search.getGroupCode()));
        String searchGroupTreeName = "";
        if (searchGroup.isPresent())
            searchGroupTreeName = searchGroup.get().getGroupTreeName();

        final List<QueueName> queueNameList = queueNameRepository.getQueueNameListByService(search, searchGroupTreeName);
        final List<StatUserInboundEntity> userInboundList = statUserInboundService.getRepository().findAllHuntStat(search, queueNameList, searchGroupTreeName);

        final List<?> dateByTypeList = SearchCycleUtils.getDateByType(search.getStartDate(), search.getEndDate(), search.getTimeUnit());

        for (Object timeInformation : dateByTypeList) {
            StatHuntResponse<?> response = null;

            if (search.getTimeUnit().equals(SearchCycle.DATE)) {
                response = new StatHuntResponse<>((DateResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.HOUR)) {
                response = new StatHuntResponse<>((HourResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.WEEK)) {
                response = new StatHuntResponse<>((WeekResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.MONTH)) {
                response = new StatHuntResponse<>((MonthResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.DAY_OF_WEEK)) {
                response = new StatHuntResponse<>((DayOfWeekResponse) timeInformation);
            }

            List<StatUserInboundEntity> statUserList = SearchCycleUtils.streamFiltering(userInboundList, search.getTimeUnit(), timeInformation);
            List<StatHuntInboundResponse> huntStatList = new ArrayList<>();
            for (QueueName hunt : queueNameList) {
                StatHuntInboundResponse row = new StatHuntInboundResponse();
                row.setQueueName(hunt.getHanName());

                statUserList.stream().filter(inbound -> inbound.getHuntNumber().equals(hunt.getNumber()))
                        .findFirst().ifPresent(entity -> {
                            row.setAvgBillSec(entity.getAvgBillSec());
                            row.setAvgRateValue(entity.getAvgRate());
                            row.setCallbackCount(entity.getCallbackSucc());
                            row.setCancel(entity.getCancel());
                            row.setInBillSecSum(entity.getInBillsecSum());
                            row.setInSuccess(entity.getInSuccess());
                            row.setInTotal(entity.getInTotal());
                            row.setServiceLevelOk(entity.getServiceLevelOk());
                });

                huntStatList.add(row);
            }

            if (response != null) {
                response.setStatQueueInboundResponses(huntStatList);
                rows.add(response);
            }
        }
        return ResponseEntity.ok(data(rows));
    }

    @GetMapping("total")
    public ResponseEntity<JsonResult<StatHuntInboundResponse>> getTotal(StatHuntSearchRequest search) {
        final Optional<CompanyTree> searchGroup = Optional.ofNullable(companyTreeRepository.findOneByGroupCode(search.getGroupCode()));
        String searchGroupTreeName = "";
        if (searchGroup.isPresent())
            searchGroupTreeName = searchGroup.get().getGroupTreeName();
        final List<QueueName> queueNameList = queueNameRepository.getQueueNameListByService(search, searchGroupTreeName);

        search.setTimeUnit(null);
        final StatUserInboundEntity entity = statUserInboundService.getRepository().findAllHuntTotalStat(search, queueNameList, searchGroupTreeName).stream()
                .findFirst().orElse(new StatUserInboundEntity());
        final StatHuntInboundResponse result = convertDto(entity, StatHuntInboundResponse.class);
        result.setAvgRateValue(entity.getAvgRate());
        result.setCallbackCount(entity.getCallbackSucc());

        return ResponseEntity.ok(data(result));
    }
}
