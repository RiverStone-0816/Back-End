package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.ApplicationBeanAware;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.model.dto.statdb.StatOutboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatOutboundTimeResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class OutboundStatExcel extends AbstractExcel {
    private final List<StatOutboundTimeResponse<?>> list;
    private final StatOutboundResponse total;

    public OutboundStatExcel(List<StatOutboundTimeResponse<?>> list, StatOutboundResponse total) {
        this.list = list;
        this.total = total;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "날짜/시간",
                "O/B", "", "",
                "성과지표", "", "");
        addRow(sheetHeadStyle, "",
                "총 시도콜", "O/B건수(성공호)", "비수신",
                "통화성공률", "O/B 총 통화시간",  "O/B 평균통화시간");

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 4, 6));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        for (StatOutboundTimeResponse<?> e : list) {
            addRow(defaultStyle, niceFormat(e.getTimeInformation()),

                    niceFormat(e.getStatOutboundResponse().getTotal()),
                    niceFormat(e.getStatOutboundResponse().getSuccess()),
                    niceFormat(e.getStatOutboundResponse().getCancel()),
                    niceFormat(e.getStatOutboundResponse().getSuccessAvg()),

                    niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(e.getStatOutboundResponse().getBillSecSum())),
                    niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(e.getStatOutboundResponse().getBillSecAvg()))
            );
        }

        addRow(defaultStyle, "합계",

                niceFormat(total.getTotal()),
                niceFormat(total.getSuccess()),
                niceFormat(total.getCancel()),
                niceFormat(String.format("%.1f", total.getSuccessAvg())),

                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getBillSecSum())),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getBillSecAvg()))
        );
    }
}
