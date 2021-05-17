package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.customdb.CommonEicnCdrResponse;

import java.util.List;

public class RecordingHistoryStatExcel extends AbstractExcel {
    final RequestGlobal g = SpringApplicationContextAware.requestGlobal();
    private final List<CommonEicnCdrResponse> list;

    public RecordingHistoryStatExcel(List<CommonEicnCdrResponse> list) {
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "대표번호", /*"고객등급",*/ "발신번호", "수신번호", "시간", "수/발신", "통화자", "호상태(초)", "부가상태", "IVR", "종료");

        for (CommonEicnCdrResponse e : list) {
            final String userName = e.getPersonList() != null && e.getPersonList().getIdName() != null && e.getPersonList().getIdName().length() > 0 ? e.getPersonList().getIdName() : null;
            addRow(defaultStyle,
                    niceFormat(e.getService() != null ? e.getService().getSvcNumber() : null),
                    /*niceFormat(e.getVipBlack()),*/
                    niceFormat(e.getSrc()),
                    niceFormat(e.getDst()),
                    niceFormat(e.getRingDate()),
                    niceFormat(e.getCallKindValue()),
                    niceFormat(userName != null ? userName : g.htmlQuote(e.getUserid())),
                    niceFormat(e.getCallStatusValue()),
                    niceFormat(e.getEtcCallResultValue()),
                    niceFormat(e.getIvrPathValue()),
                    niceFormat(e.getCallingHangupValue())
            );
        }
    }
}
