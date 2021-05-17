package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.statdb.TalkStatisticsPersonResponse;
import kr.co.eicn.ippbx.server.model.search.TalkStatisticsSearchRequest;

import java.util.List;

public class TalkPersonStatExcel extends AbstractExcel {
    private TalkStatisticsSearchRequest search;
    private List<TalkStatisticsPersonResponse> list;

    public TalkPersonStatExcel(TalkStatisticsSearchRequest search, List<TalkStatisticsPersonResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "상담원", "개설대화방수", "종료대화방수", "수신메시지수", "발신메시지수", "자동멘트수", "초과자동멘트수");
        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (TalkStatisticsPersonResponse e : list) {
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
                niceFormat(list.stream().mapToInt(TalkStatisticsPersonResponse::getStartRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsPersonResponse::getEndRoomCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsPersonResponse::getInMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsPersonResponse::getOutMsgCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsPersonResponse::getAutoMentCnt).sum()),
                niceFormat(list.stream().mapToInt(TalkStatisticsPersonResponse::getAutoMentExceedCnt).sum())
        );
    }
}
