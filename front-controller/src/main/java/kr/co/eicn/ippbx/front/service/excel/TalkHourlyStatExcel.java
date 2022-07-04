package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsHourlyResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;

import java.util.List;

public class TalkHourlyStatExcel extends AbstractExcel {
    private TalkStatisticsSearchRequest search;
    private List<WtalkStatisticsHourlyResponse> list;

    public TalkHourlyStatExcel(TalkStatisticsSearchRequest search, List<WtalkStatisticsHourlyResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "시간", "개설대화방수", "종료대화방수", "수신메시지수", "발신메시지수", "자동멘트수");
        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (WtalkStatisticsHourlyResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getStatHour()),
                    niceFormat(e.getStartRoomCnt()),
                    niceFormat(e.getEndRoomCnt()),
                    niceFormat(e.getInMsgCnt()),
                    niceFormat(e.getOutMsgCnt()),
                    niceFormat(e.getAutoMentCnt())
            );
        }
        addRow(sheetHeadStyle, "합계",
                niceFormat(list.stream().mapToInt(WtalkStatisticsHourlyResponse::getStartRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsHourlyResponse::getEndRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsHourlyResponse::getInMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsHourlyResponse::getOutMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsHourlyResponse::getAutoMentCnt).sum())
        );
    }
}
