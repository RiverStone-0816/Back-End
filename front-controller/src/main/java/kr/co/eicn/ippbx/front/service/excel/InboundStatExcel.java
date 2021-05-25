package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.ApplicationBeanAware;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.model.dto.statdb.StatInboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatInboundTimeResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class InboundStatExcel extends AbstractExcel {
    private final List<StatInboundTimeResponse<?>> list;
    private final StatInboundResponse total;

    public InboundStatExcel(List<StatInboundTimeResponse<?>> list, StatInboundResponse total) {
        this.list = list;
        this.total = total;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "날짜/시간",
                "I/B 콜 현황", "", "", "", "",
                "성과지표", "", "", "", "", "",
                "응대호 대기시간 분석", "", "", "", "",
                "포기호 대기시간 분석", "", "", "", "");
        addRow(sheetHeadStyle, "",
                "I/B 전체콜", "단순조회", "연결요청", "응대호", "포기호", "콜백",
                "I/B 총통화시간", "평균통화시간", "평균대기시간", "호응답률", "서비스레벨 호응답률", "단순조회율",
                "~10(초)", "~20(초)", "~30(초)", "~40(초)", "40~(초)",
                "~10(초)", "~20(초)", "~30(초)", "~40(초)", "40~(초)");

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 7, 12));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 13, 17));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 18, 22));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        for (StatInboundTimeResponse<?> e : list) {
            addRow(defaultStyle, niceFormat(e.getTimeInformation()),

                    niceFormat(e.getInboundStat().getTotal()),
                    niceFormat(e.getInboundStat().getOnlyRead()),
                    niceFormat(e.getInboundStat().getConnReq()),
                    niceFormat(e.getInboundStat().getSuccess()),
                    niceFormat(e.getInboundStat().getCancel()),
                    niceFormat(e.getInboundStat().getCallbackSuccess()),

                    niceFormat(g.timeFormatFromSeconds(e.getInboundStat().getBillSecSum())),
                    niceFormat(g.timeFormatFromSeconds(e.getInboundStat().getBillSecAvg())),
                    niceFormat(g.timeFormatFromSeconds(e.getInboundStat().getWaitAvg())),
                    niceFormat(e.getInboundStat().getResponseRate()),
                    niceFormat(e.getInboundStat().getSvcLevelAvg()),
                    niceFormat(e.getInboundStat().getIvrAvg()),

                    niceFormat(e.getInboundStat().getWaitSucc_0_10()),
                    niceFormat(e.getInboundStat().getWaitSucc_10_20()),
                    niceFormat(e.getInboundStat().getWaitSucc_20_30()),
                    niceFormat(e.getInboundStat().getWaitSucc_30_40()),
                    niceFormat(e.getInboundStat().getWaitSucc_40()),

                    niceFormat(e.getInboundStat().getWaitCancel_0_10()),
                    niceFormat(e.getInboundStat().getWaitCancel_10_20()),
                    niceFormat(e.getInboundStat().getWaitCancel_20_30()),
                    niceFormat(e.getInboundStat().getWaitCancel_30_40()),
                    niceFormat(e.getInboundStat().getWaitCancel_40())
            );
        }

        addRow(defaultStyle, "합계",

                niceFormat(total.getTotal()),
                niceFormat(total.getOnlyRead()),
                niceFormat(total.getConnReq()),
                niceFormat(total.getSuccess()),
                niceFormat(total.getCancel()),
                niceFormat(total.getCallbackSuccess()),

                niceFormat(g.timeFormatFromSeconds(total.getBillSecSum())),
                niceFormat(g.timeFormatFromSeconds(total.getBillSecAvg())),
                niceFormat(g.timeFormatFromSeconds(total.getWaitAvg())),
                niceFormat(String.format("%.1f", total.getResponseRate())),
                niceFormat(String.format("%.1f", total.getSvcLevelAvg())),
                niceFormat(String.format("%.1f", total.getIvrAvg())),

                niceFormat(total.getWaitSucc_0_10()),
                niceFormat(total.getWaitSucc_10_20()),
                niceFormat(total.getWaitSucc_20_30()),
                niceFormat(total.getWaitSucc_30_40()),
                niceFormat(total.getWaitSucc_40()),

                niceFormat(total.getWaitCancel_0_10()),
                niceFormat(total.getWaitCancel_10_20()),
                niceFormat(total.getWaitCancel_20_30()),
                niceFormat(total.getWaitCancel_30_40()),
                niceFormat(total.getWaitCancel_40())
        );
    }
}
