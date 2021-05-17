package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.statdb.TalkStatisticsHourlyResponse;
import kr.co.eicn.ippbx.server.model.search.TalkStatisticsSearchRequest;

import java.util.List;

public class TalkHourlyStatExcel extends AbstractExcel {
    private TalkStatisticsSearchRequest search;
    private List<TalkStatisticsHourlyResponse> list;

    public TalkHourlyStatExcel(TalkStatisticsSearchRequest search, List<TalkStatisticsHourlyResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "시간", "개설대화방수", "종료대화방수", "수신메시지수", "발신메시지수", "자동멘트수", "초과자동멘트수");
        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (TalkStatisticsHourlyResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getStatHour()),
                    niceFormat(e.getStartRoomCnt()),
                    niceFormat(e.getEndRoomCnt()),
                    niceFormat(e.getInMsgCnt()),
                    niceFormat(e.getOutMsgCnt()),
                    niceFormat(e.getAutoMentCnt()),
                    niceFormat(e.getAutoMentExceedCnt())
            );
        }
        addRow(sheetHeadStyle, "합계",
                niceFormat(list.stream().mapToInt(TalkStatisticsHourlyResponse::getStartRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsHourlyResponse::getEndRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsHourlyResponse::getInMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsHourlyResponse::getOutMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsHourlyResponse::getAutoMentCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsHourlyResponse::getAutoMentExceedCnt).sum())
        );
    }
}
