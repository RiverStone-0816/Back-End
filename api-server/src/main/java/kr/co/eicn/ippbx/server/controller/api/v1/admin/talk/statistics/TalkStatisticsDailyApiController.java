package kr.co.eicn.ippbx.server.controller.api.v1.admin.talk.statistics;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.dto.statdb.TalkStatisticsDailyResponse;
import kr.co.eicn.ippbx.model.entity.statdb.StatTalkEntity;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;
import kr.co.eicn.ippbx.server.service.TalkStatisticsService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡통계 > 상담톡일별통계
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/talk/statistics/daily", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkStatisticsDailyApiController extends ApiBaseController {
    private final TalkStatisticsService talkStatisticsService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<TalkStatisticsDailyResponse>>> list(TalkStatisticsSearchRequest search) {
        if (search.getStartDate() != null && search.getEndDate() != null)
            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");

        final List<StatTalkEntity> list = talkStatisticsService.getRepository().dailyStatList(search);
        final List<TalkStatisticsDailyResponse> rows = list.stream()
                .map(e -> convertDto(e, TalkStatisticsDailyResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(rows));
    }
}
