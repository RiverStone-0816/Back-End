package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.eicn.CallbackHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.CallbackHistorySearchRequest;

import java.util.List;

public class CallbackHistoryExcel extends AbstractExcel {
    private CallbackHistorySearchRequest search;
    private List<CallbackHistoryResponse> list;

    public CallbackHistoryExcel(CallbackHistorySearchRequest search, List<CallbackHistoryResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "수신번호", "콜백번호", "인입서비스", "인입큐", "상담원", "처리상태", "입력일시", "처리일시");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();
        for (CallbackHistoryResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getCallerNumber()),
                    niceFormat(e.getCallbackNumber()),
                    niceFormat(e.getSvcName()),
                    niceFormat(e.getQueueName()),
                    niceFormat(e.getIdName()),
                    e.getStatus() != null ? message.getEnumText(e.getStatus()) : "",
                    niceFormat(e.getInputDate()),
                    niceFormat(e.getResultDate())
            );
        }
    }
}
