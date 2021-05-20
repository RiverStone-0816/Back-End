package kr.co.eicn.ippbx.server.controller.api.v1.admin.stat.outbound;

import kr.co.eicn.ippbx.model.dto.statdb.StatOutboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatOutboundTimeResponse;
import kr.co.eicn.ippbx.model.dto.util.*;
import kr.co.eicn.ippbx.model.entity.statdb.StatOutboundEntity;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatOutboundSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.StatOutboundService;
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
 * 통계관리 > 아웃바운드통계 > 아웃바운드콜실적
 **/

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stat/outbound", produces = MediaType.APPLICATION_JSON_VALUE)
public class StatOutboundApiController extends ApiBaseController {
    private final StatOutboundService statOutboundService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<StatOutboundTimeResponse<?>>>> list(StatOutboundSearchRequest search) {
        if (search.getStartDate().after(search.getEndDate()))
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));
        if ((search.getEndDate().getTime() - search.getStartDate().getTime()) / 1000 > 6 * 30 * 24 * 60 * 60)
            throw new IllegalArgumentException(message.getText("messages.validator.enddate.indays", "180일"));

        final List<StatOutboundTimeResponse<?>> rows = new ArrayList<>();
        final List<StatOutboundEntity> outboundList = statOutboundService.getRepository().findAll(search);

        final List<?> dateByTypeList = SearchCycleUtils.getDateByType(search.getStartDate(), search.getEndDate(), search.getTimeUnit());

        for (Object timeInformation : dateByTypeList) {
            List<StatOutboundEntity> statOutboundList = SearchCycleUtils.streamFiltering(outboundList, search.getTimeUnit(), timeInformation);
            StatOutboundTimeResponse<?> statOutbound = null;

            if (search.getTimeUnit().equals(SearchCycle.DATE)) {
                statOutbound = new StatOutboundTimeResponse<>((DateResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.HOUR)) {
                statOutbound = new StatOutboundTimeResponse<>((HourResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.WEEK)) {
                statOutbound = new StatOutboundTimeResponse<>((WeekResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.MONTH)) {
                statOutbound = new StatOutboundTimeResponse<>((MonthResponse) timeInformation);
            } else if (search.getTimeUnit().equals(SearchCycle.DAY_OF_WEEK)) {
                statOutbound = new StatOutboundTimeResponse<>((DayOfWeekResponse) timeInformation);
            }

            if (statOutbound != null) {
                statOutbound.setStatOutboundResponse(
                        statOutboundList.stream().map(entity -> convertDto(entity, StatOutboundResponse.class)).findFirst().orElse(new StatOutboundResponse())
                );
            }

            rows.add(statOutbound);
        }
        return ResponseEntity.ok(data(rows));
    }

    @GetMapping("total")
    public ResponseEntity<JsonResult<StatOutboundResponse>> total(StatOutboundSearchRequest search) {
        search.setTimeUnit(null);
        final StatOutboundEntity entity = statOutboundService.getRepository().findAll(search).stream().findFirst().orElse(new StatOutboundEntity());
        final StatOutboundResponse result = convertDto(entity, StatOutboundResponse.class);

        return ResponseEntity.ok(data(result));
    }
}
