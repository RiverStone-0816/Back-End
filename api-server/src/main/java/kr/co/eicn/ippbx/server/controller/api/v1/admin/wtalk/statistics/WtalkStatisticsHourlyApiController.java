package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.statistics;

import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsHourlyResponse;
import kr.co.eicn.ippbx.model.entity.statdb.StatWtalkEntity;
import kr.co.eicn.ippbx.model.search.TalkStatisticsHourlySearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.WtalkStatisticsService;
import kr.co.eicn.ippbx.util.JsonResult;
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
 * 상담톡관리 > 상담톡통계 > 상담톡시간별통계
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/statistics/hourly", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkStatisticsHourlyApiController extends ApiBaseController {
    private final WtalkStatisticsService wtalkStatisticsService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<WtalkStatisticsHourlyResponse>>> list(TalkStatisticsHourlySearchRequest search) {
        if (search.getStartDate() != null && search.getEndDate() != null) {
            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");
            if (search.getStartHour() != null && search.getEndHour() != null)
                if (search.getStartHour() > search.getEndHour())
                    throw new IllegalArgumentException(message.getText("messages.validator.endhour.after.starthour"));
        }

        final List<StatWtalkEntity> list = wtalkStatisticsService.getRepository().hourlyStatList(search);
        final List<WtalkStatisticsHourlyResponse> rows = new ArrayList<>();

        for (byte i = 0; i < 24; i++) {
            final byte hour = i;
            final Optional<StatWtalkEntity> stat = list.stream().filter(e -> e.getStatHour().equals(hour)).findFirst();

            if (stat.isPresent()) {
                rows.add(convertDto(stat.get(), WtalkStatisticsHourlyResponse.class));
            } else {
                final WtalkStatisticsHourlyResponse row = new WtalkStatisticsHourlyResponse();

                row.setStatHour(i);
                row.setAutoMentCnt(0);
                row.setAutoMentExceedCnt(0);
                row.setEndRoomCnt(0);
                row.setInMsgCnt(0);
                row.setOutMsgCnt(0);
                row.setStartRoomCnt(0);

                rows.add(row);
            }
        }

        return ResponseEntity.ok(data(rows));
    }
}
