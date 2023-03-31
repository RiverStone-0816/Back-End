package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.model.dto.eicn.SendMessageHistoryResponse;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;

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
        addRow(sheetHeadStyle,"수신번호", "문구내용", "발송구분", "발송여부", "발송일");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (SendMessageHistoryResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getTarget()),
                    niceFormat(e.getContent()),
                    niceFormat(e.getSendType()),
                    niceFormat(e.getResMessage()),
                    niceFormat(e.getSendDate())
            );
        }
    }
}
