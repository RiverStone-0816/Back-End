package kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.inbound;

import kr.co.eicn.ippbx.model.dto.statdb.StatInboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatInboundTimeResponse;
import kr.co.eicn.ippbx.model.dto.util.*;
import kr.co.eicn.ippbx.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatInboundSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.StatInboundService;
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
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 통계관리 > 인바운드통계 > 인바운드통계
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stat/inbound", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatInboundApiController extends ApiBaseController {
    private final StatInboundService statInboundService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<StatInboundTimeResponse<?>>>> list(StatInboundSearchRequest search) {
        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
        if ((search.getEndDate().getTime() - search.getStartDate().getTime()) / 1000 > 6 * 30 * 24 * 60 * 60)
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.indays", "180일"));

        final List<StatInboundTimeResponse<?>> rows = new ArrayList<>();

        final List<StatInboundEntity> inboundList = statInboundService.getRepository().findAll(search);

        final List<?> dateByTypeList = SearchCycleUtils.getDateByType(search.getStartDate(), search.getEndDate(), search.getTimeUnit());

        for (Object timeInformation : dateByTypeList) {
            List<StatInboundEntity> statInboundList = SearchCycleUtils.streamFiltering(inboundList, search.getTimeUnit(), timeInformation);
            StatInboundTimeResponse<?> inboundResponse = null;

            if (search.getTimeUnit().equals(SearchCycle.DATE)) {
                inboundResponse = new StatInboundTimeResponse<>((DateResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.HOUR)) {
                inboundResponse = new StatInboundTimeResponse<>((HourResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.WEEK)) {
                inboundResponse = new StatInboundTimeResponse<>((WeekResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.MONTH)) {
                inboundResponse = new StatInboundTimeResponse<>((MonthResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.DAY_OF_WEEK)) {
                inboundResponse = new StatInboundTimeResponse<>((DayOfWeekResponse) timeInformation);
            }

            if (inboundResponse != null) {
                inboundResponse.setInboundStat(
                        statInboundList.stream().map(entity -> convertDto(entity, StatInboundResponse.class))
                                .findFirst().orElse(new StatInboundResponse())
                );
            }
            rows.add(inboundResponse);
        }

        return ResponseEntity.ok(data(rows));
    }

    @GetMapping("total")
    public ResponseEntity<JsonResult<StatInboundResponse>> getTotal(StatInboundSearchRequest search) {
        search.setTimeUnit(null);
        final StatInboundEntity entity = statInboundService.getRepository().findAll(search).stream().findFirst().orElse(new StatInboundEntity());
        final StatInboundResponse result = convertDto(entity, StatInboundResponse.class);

        result.setBillSecSum(entity.getBillsecSum());
        result.setWaitAvg(entity.getWaitAvg());

        return ResponseEntity.ok(data(result));
    }
}
