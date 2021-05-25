package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.ApplicationBeanAware;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.model.dto.eicn.TalkRoomResponse;

import java.util.List;

public class TalkHistoryStatExcel extends AbstractExcel {
    final RequestGlobal g = ApplicationBeanAware.requestGlobal();
    private final List<TalkRoomResponse> list;

    public TalkHistoryStatExcel(List<TalkRoomResponse> list) {
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "대화방명", "상담톡서비스", "대화방상태", "상담원", "시작시간", "마지막메시지시간", "고객명");
        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (TalkRoomResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getRoomName()),
                    niceFormat(e.getSenderKey()),
                    niceFormat(g.messageOf("RoomStatus", e.getRoomStatus())),
                    niceFormat(e.getIdName()),
                    niceFormat(e.getRoomStartTime()),
                    niceFormat(e.getRoomLastTime()),
                    niceFormat(e.getIdName())
            );
        }
    }
}
