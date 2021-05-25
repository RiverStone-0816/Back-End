package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.ApplicationBeanAware;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.model.dto.statdb.StatTotalRow;
import kr.co.eicn.ippbx.model.dto.util.DateResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class TotalStatExcel extends AbstractExcel {
    private final List<StatTotalRow<?>> list;
    private final StatTotalRow<DateResponse> total;

    public TotalStatExcel(List<StatTotalRow<?>> list, StatTotalRow<DateResponse> total) {
        this.list = list;
        this.total = total;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "날짜/시간", "총통화", "", "O/B", "", "", "", "", "", "I/B", "", "", "", "", "", "", "", "", "", "");
        addRow(sheetHeadStyle, "", "총건수", "총시간",
                "총시도콜", "O/B건수 (성공호)", "비수신", "통화성공률", "O/B (총통화시간)", "O/B (평균통화시간)",
                "I/B (전체콜)", "단순조회", "연결요청", "응대호", "포기호", "콜백", "I/B (총통화시간)", "I/B (통화시간)", "I/B (대기시간)", "호응답률", "단순조회율");

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 8));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 9, 19));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        for (StatTotalRow<?> row : list) {
            addRow(defaultStyle,
                    niceFormat(row.getTimeInformation().toString()),

                    niceFormat(row.getTotalCount()),
                    g.timeFormatFromSeconds(row.getTotalBillSec()),

                    niceFormat(row.getOutboundStat().getTotal()),
                    niceFormat(row.getOutboundStat().getSuccess()),
                    niceFormat(row.getOutboundStat().getCancel()),
                    niceFormat(row.getOutboundStat().getSuccessAvg()),
                    g.timeFormatFromSeconds(row.getOutboundStat().getBillSecSum()),
                    g.timeFormatFromSeconds(row.getOutboundStat().getBillSecAvg()),

                    niceFormat(row.getInboundStat().getTotal()),
                    niceFormat(row.getInboundStat().getOnlyRead()),
                    niceFormat(row.getInboundStat().getConnReq()),
                    niceFormat(row.getInboundStat().getSuccess()),
                    niceFormat(row.getInboundStat().getCancel()),
                    niceFormat(row.getInboundStat().getCallbackSuccess()),
                    g.timeFormatFromSeconds(row.getInboundStat().getBillSecSum()),
                    g.timeFormatFromSeconds(row.getInboundStat().getBillSecAvg()),
                    g.timeFormatFromSeconds(row.getInboundStat().getWaitAvg()),
                    niceFormat(row.getInboundStat().getResponseRate()),
                    niceFormat(row.getInboundStat().getIvrAvg())
            );
        }

        addRow(defaultStyle,
                "합계",

                niceFormat(total.getTotalCount()),
                g.timeFormatFromSeconds(total.getTotalBillSec()),

                niceFormat(total.getOutboundStat().getTotal()),
                niceFormat(total.getOutboundStat().getSuccess()),
                niceFormat(total.getOutboundStat().getCancel()),
                niceFormat(String.format("%.1f", total.getOutboundStat().getSuccessAvg())),
                g.timeFormatFromSeconds(total.getOutboundStat().getBillSecSum()),
                g.timeFormatFromSeconds(total.getOutboundStat().getBillSecAvg()),

                niceFormat(total.getInboundStat().getTotal()),
                niceFormat(total.getInboundStat().getOnlyRead()),
                niceFormat(total.getInboundStat().getConnReq()),
                niceFormat(total.getInboundStat().getSuccess()),
                niceFormat(total.getInboundStat().getCancel()),
                niceFormat(total.getInboundStat().getCallbackSuccess()),
                g.timeFormatFromSeconds(total.getInboundStat().getBillSecSum()),
                g.timeFormatFromSeconds(total.getInboundStat().getBillSecAvg()),
                g.timeFormatFromSeconds(total.getInboundStat().getWaitAvg()),
                niceFormat(String.format("%.1f", total.getInboundStat().getResponseRate())),
                niceFormat(String.format("%.1f", total.getInboundStat().getIvrAvg()))
        );
    }
}
