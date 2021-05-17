package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.statistics;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.model.dto.statdb.TalkStatisticsDailyResponse;
import kr.co.eicn.ippbx.server.model.entity.statdb.StatTalkEntity;
import kr.co.eicn.ippbx.server.model.search.TalkStatisticsSearchRequest;
import kr.co.eicn.ippbx.server.service.TalkStatisticsService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡통계 > 상담톡일별통계
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/statistics/daily", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkStatisticsDailyApiController extends ApiBaseController {
    private final TalkStatisticsService talkStatisticsService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<TalkStatisticsDailyResponse>>> list(TalkStatisticsSearchRequest search) {
        if (search.getStartDate() != null && search.getEndDate() != null)
            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException(message.getText("messages.validator.enddate.after.startdate"));

        final List<StatTalkEntity> list = talkStatisticsService.getRepository().dailyStatList(search);
        final List<TalkStatisticsDailyResponse> rows = list.stream()
                .map(e -> convertDto(e, TalkStatisticsDailyResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(rows));
    }
}
