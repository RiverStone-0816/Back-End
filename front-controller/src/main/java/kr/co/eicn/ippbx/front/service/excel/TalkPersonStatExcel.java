package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsPersonResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;

import java.util.List;

public class TalkPersonStatExcel extends AbstractExcel {
    private TalkStatisticsSearchRequest search;
    private List<WtalkStatisticsPersonResponse> list;

    public TalkPersonStatExcel(TalkStatisticsSearchRequest search, List<WtalkStatisticsPersonResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "상담원", "개설대화방수", "종료대화방수", "수신메시지수", "발신메시지수", "자동멘트수", "초과자동멘트수");
        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (WtalkStatisticsPersonResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getIdName()),
                    niceFormat(e.getStartRoomCnt()),
                    niceFormat(e.getEndRoomCnt()),
                    niceFormat(e.getInMsgCnt()),
                    niceFormat(e.getOutMsgCnt()),
                    niceFormat(e.getAutoMentCnt()),
                    niceFormat(e.getAutoMentExceedCnt())
            );
        }
        addRow(sheetHeadStyle, "합계",
                niceFormat(list.stream().mapToInt(WtalkStatisticsPersonResponse::getStartRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsPersonResponse::getEndRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsPersonResponse::getInMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsPersonResponse::getOutMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsPersonResponse::getAutoMentCnt).sum()),
                niceFormat(list.stream().mapToInt(WtalkStatisticsPersonResponse::getAutoMentExceedCnt).sum())
        );
    }
}
