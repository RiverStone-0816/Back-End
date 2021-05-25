package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.model.dto.eicn.EmailConsultationHistorySummaryResponse;
import kr.co.eicn.ippbx.model.search.EmailConsultationHistorySearchRequest;

import java.util.List;

public class EmailConsultationHistoryExcel extends AbstractExcel {
    private EmailConsultationHistorySearchRequest search;
    private List<EmailConsultationHistorySummaryResponse> list;

    public EmailConsultationHistoryExcel(EmailConsultationHistorySearchRequest search, List<EmailConsultationHistorySummaryResponse> list) {
        this.search = search;
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "분배", "이관", "처리여부", "관련서비스", "상담종류", "메일수신시간", "발신자명", "발신이메일", "제목", "고객명", "고객사정보");

        for (EmailConsultationHistorySummaryResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getUserName()),
                    niceFormat(e.getUserTrName()),
                    niceFormat(e.getResultCodeName()),
                    niceFormat(e.getResultServiceName()),
                    niceFormat(e.getResultKindName()),
                    niceFormat(e.getSentDate()),
                    niceFormat(e.getFromEmail()),
                    niceFormat(e.getFromName()),
                    niceFormat(e.getSubject()),
                    niceFormat(e.getCustomName()),
                    niceFormat(e.getCustomCompanyName())
            );
        }
    }
}
