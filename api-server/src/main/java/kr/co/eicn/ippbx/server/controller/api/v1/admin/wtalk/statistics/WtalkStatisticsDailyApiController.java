package kr.co.eicn.ippbx.server.controller.api.v1.admin.wtalk.statistics;

import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsDailyResponse;
import kr.co.eicn.ippbx.model.entity.statdb.StatWtalkEntity;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담톡관리 > 상담톡통계 > 상담톡일별통계
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/wtalk/statistics/daily", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkStatisticsDailyApiController extends ApiBaseController {
    private final WtalkStatisticsService wtalkStatisticsService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<WtalkStatisticsDailyResponse>>> list(TalkStatisticsSearchRequest search) {
        if (search.getStartDate() != null && search.getEndDate() != null)
            if (search.getStartDate().after(search.getEndDate()))
                throw new IllegalArgumentException("시작시간이 종료시간보다 이전이어야 합니다.");

        final List<WtalkStatisticsDailyResponse> rows = new ArrayList<>();
        final List<StatWtalkEntity> list = wtalkStatisticsService.getRepository().dailyStatList(search);
        final List<Date> dates = list.stream().map(StatWtalkEntity::getStatDate).distinct().collect(Collectors.toList());

        for (Date date : dates){
            WtalkStatisticsDailyResponse row = new WtalkStatisticsDailyResponse();
            row.setStatDate(date.toString());

            list.stream().filter(e -> e.getStatDate().equals(date)).forEach(e -> {
                switch (e.getActionType()){
                    case "START_ROOM": // 개설대화방수
                        row.setStartRoomCnt(row.getStartRoomCnt() + e.getCnt());
                        break;
                    case "USER_END_ROOM": case "CUSTOM_END_ROOM": // 종료대화방수
                        row.setEndRoomCnt(row.getEndRoomCnt() + e.getCnt());
                        break;
                    case "RECEIVE_MSG": case "RECEIVE_FILE": // 수신메시지수
                        row.setInMsgCnt(row.getInMsgCnt() + e.getCnt());
                        break;
                    case "SEND_MSG": case "SEND_FILE": // 발신메시지수
                        row.setOutMsgCnt(row.getOutMsgCnt() + e.getCnt());
                        break;
                    case "AUTOMSG_SA": case "AUTOMSG_AW": case "AUTOMSG_AE":// 자동멘트수
                        row.setAutoMentCnt(row.getAutoMentCnt() + e.getCnt());
                        break;
                }
            });

            rows.add(row);
        }

        return ResponseEntity.ok(data(rows));
    }
}
