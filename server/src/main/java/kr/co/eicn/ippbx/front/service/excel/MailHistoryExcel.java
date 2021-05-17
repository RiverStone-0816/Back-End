package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendFaxEmailHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.SendFaxEmailHistorySearchRequest;

import java.util.List;

public class MailHistoryExcel extends AbstractExcel {
    private SendFaxEmailHistorySearchRequest search;
    private List<SendFaxEmailHistoryResponse> list;

    public MailHistoryExcel(SendFaxEmailHistorySearchRequest search, List<SendFaxEmailHistoryResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "수신인", "수신번호", "카테고리", "발송매체", "유형설명", "발송구분", "발송일");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (SendFaxEmailHistoryResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getReceiver()),
                    niceFormat(e.getReceiverNumber().stream().map(e2 -> e2.getPhone() != null && e2.getPhone().length() > 0 ? e2.getPhone() : e2.getTarget()).reduce((string, target) -> string + ", " + target).orElse("")),
                    niceFormat(e.getCategoryName()),
                    niceFormat(e.getType()),
                    niceFormat(e.getContent()),
                    niceFormat(e.getSendSort()),
                    niceFormat(e.getSendDate())
            );
        }
    }
}
