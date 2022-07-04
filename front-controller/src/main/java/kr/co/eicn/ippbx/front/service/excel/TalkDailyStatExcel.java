package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsDailyResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;

import java.util.List;

public class TalkDailyStatExcel extends AbstractExcel {
    private TalkStatisticsSearchRequest search;
    private List<WtalkStatisticsDailyResponse> list;

    public TalkDailyStatExcel(TalkStatisticsSearchRequest search, List<WtalkStatisticsDailyResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "날짜", "개설대화방수", "종료대화방수", "수신메시지수", "발신메시지수", "자동멘트수");
        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (WtalkStatisticsDailyResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getStatDate()),
                    niceFormat(e.getStartRoomCnt()),
                    niceFormat(e.getEndRoomCnt()),
                    niceFormat(e.getInMsgCnt()),
                    niceFormat(e.getOutMsgCnt()),
                    niceFormat(e.getAutoMentCnt())
            );
        }
        addRow(sheetHeadStyle, "합계",
                niceFormat(list.stream().mapToInt(WtalkStatisticsDailyResponse::getStartRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsDailyResponse::getEndRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsDailyResponse::getInMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsDailyResponse::getOutMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsDailyResponse::getAutoMentCnt).sum())
        );
    }
}
