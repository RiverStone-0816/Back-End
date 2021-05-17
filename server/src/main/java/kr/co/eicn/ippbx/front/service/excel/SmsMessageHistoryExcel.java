package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendMessageHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.SendMessageSearchRequest;

import java.util.List;

public class SmsMessageHistoryExcel extends AbstractExcel {
    private SendMessageSearchRequest search;
    private List<SendMessageHistoryResponse> list;

    public SmsMessageHistoryExcel(SendMessageSearchRequest search, List<SendMessageHistoryResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "수신인", "수신번호", "문구내용", "발송구분", "발송일");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (SendMessageHistoryResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getReceiver()),
                    niceFormat(e.getTarget()),
                    niceFormat(e.getContent()),
                    niceFormat(e.getSendSort()),
                    niceFormat(e.getSendDate())
            );
        }
    }
}
