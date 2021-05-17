package kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.total;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.model.dto.statdb.*;
import kr.co.eicn.ippbx.server.model.dto.util.*;
import kr.co.eicn.ippbx.server.model.entity.statdb.*;
import kr.co.eicn.ippbx.server.model.enums.SearchCycle;
import kr.co.eicn.ippbx.server.model.search.StatTotalSearchRequest;
import kr.co.eicn.ippbx.server.service.StatInboundService;
import kr.co.eicn.ippbx.server.service.StatOutboundService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.SearchCycleUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 통계관리 > 총통화통계 > 총통화통계
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stat/total", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatTotalApiController extends ApiBaseController {
    private final StatInboundService inboundService;
    private final StatOutboundService outboundService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<StatTotalRow<?>>>> list(StatTotalSearchRequest search) {
        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
        if ((search.getEndDate().getTime() - search.getStartDate().getTime()) / 1000 > 6 * 30 * 24 * 60 * 60)
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.indays", "180일"));

        final List<StatTotalRow<?>> rows = new ArrayList<>();

        final List<StatOutboundEntity> outboundList = outboundService.getRepository().findAll(search);
        final List<StatInboundEntity> inboundList = inboundService.getRepository().findAllTotal(search);

        List<?> dateByTypeList = SearchCycleUtils.getDateByType(search.getStartDate(), search.getEndDate(), search.getTimeUnit());

        for (Object timeInformation : dateByTypeList) {
            StatTotalRow<?> row = null;

            List<StatOutboundEntity> statOutboundList = SearchCycleUtils.streamFiltering(outboundList, search.getTimeUnit(), timeInformation);
            List<StatInboundEntity> statInboundList = SearchCycleUtils.streamFiltering(inboundList, search.getTimeUnit(), timeInformation);

            if (search.getTimeUnit().equals(SearchCycle.DATE)) {
                row = new StatTotalRow<>((DateResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.HOUR)) {
                row = new StatTotalRow<>((HourResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.WEEK)) {
                row = new StatTotalRow<>((WeekResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.MONTH)) {
                row = new StatTotalRow<>((MonthResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.DAY_OF_WEEK)) {
                row = new StatTotalRow<>((DayOfWeekResponse) timeInformation);
            }

            if (row != null) {
                row.setInboundStat(
                        statInboundList.stream().map(inbound -> convertDto(inbound, StatInboundResponse.class)).findFirst().orElse(new StatInboundResponse())
                );

                row.setOutboundStat(
                        statOutboundList.stream().map(outbound -> convertDto(outbound, StatOutboundResponse.class)).findFirst().orElse(new StatOutboundResponse())
                );

                rows.add(row);
            }
        }
        return ResponseEntity.ok(data(rows));
    }

    @GetMapping("total")
    public ResponseEntity<JsonResult<StatTotalRow<DateResponse>>> getTotal(StatTotalSearchRequest search) {
        search.setTimeUnit(null);
        StatTotalRow<DateResponse> result = new StatTotalRow<>();
        result.setTimeInformation(null);

        outboundService.getRepository().findAll(search).stream().findFirst()
                .ifPresent(outbound -> result.setOutboundStat(convertDto(outbound, StatOutboundResponse.class)));
        inboundService.getRepository().findAllTotal(search).stream().findFirst()
                .ifPresent(inbound -> result.setInboundStat(convertDto(inbound, StatInboundResponse.class)));

        return ResponseEntity.ok(data(result));
    }
}