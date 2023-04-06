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
                "I/B 콜 현황", "", "", "", "", "", "", "", "",
                "성과지표", "", "", "", "", "", "", "", "",
                "응대호 대기시간(SEC) 분석", "", "", "", "",
                "포기호 대기시간(SEC) 분석", "", "", "", "");
        addRow(sheetHeadStyle, "",
                "I/B 전체콜", "단순조회", "연결요청", "응대호", "포기호", "", "", "", "콜백",
                "I/B 총 통화시간", "I/B 평균통화시간", "I/B 평균대기시간", "호응답률", "콜백포함 응답률", "서비스레벨 호응답률", "양적개선 응답률", "질적개선 응답률", "단순조회율",
                "~10", "~20", "~30", "~40", "40~",
                "~10", "~20", "~30", "~40", "40~");
        addRow(sheetHeadStyle, "", "", "", "", "", "전체", "비수신 포기호", "타임아웃 포기호", "고객 포기호",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

        getSheet().addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 1, 9));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 10, 18));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 19, 23));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 24, 28));
        getSheet().addMergedRegion(new CellRangeAddress(1, 1, 5, 8));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 9, 9));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 10, 10));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 11, 11));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 12, 12));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 17, 17));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 18, 18));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 19, 19));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 20, 20));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 21, 21));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 22, 22));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 23, 23));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 24, 24));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 25, 25));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 26, 26));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 27, 27));
        getSheet().addMergedRegion(new CellRangeAddress(1, 2, 28, 28));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        for (StatInboundTimeResponse<?> e : list) {
            addRow(defaultStyle, niceFormat(e.getTimeInformation()),

                    niceFormat(e.getInboundStat().getTotal()),
                    niceFormat(e.getInboundStat().getOnlyRead()),
                    niceFormat(e.getInboundStat().getConnReq()),
                    niceFormat(e.getInboundStat().getSuccess()),
                    niceFormat(e.getInboundStat().getCancel()),
                    niceFormat(e.getInboundStat().getCancelNoAnswer()),
                    niceFormat(e.getInboundStat().getCancelTimeout()),
                    niceFormat(e.getInboundStat().getCancelCustom()),
                    niceFormat(e.getInboundStat().getCallback()),

                    niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(e.getInboundStat().getBillSecSum())),
                    niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(e.getInboundStat().getBillSecAvg())),
                    niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(e.getInboundStat().getWaitAvg())),
                    niceFormat(e.getInboundStat().getResponseRate()),
                    niceFormat(e.getInboundStat().getCallbackResponseRate()),
                    niceFormat(e.getInboundStat().getSvcLevelAvg()),
                    niceFormat(e.getInboundStat().getQuantitativeResponseRate()),
                    niceFormat(e.getInboundStat().getQualitativeResponseRate()),
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
                niceFormat(total.getCancelNoAnswer()),
                niceFormat(total.getCancelTimeout()),
                niceFormat(total.getCancelCustom()),
                niceFormat(total.getCallback()),

                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getBillSecSum())),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getBillSecAvg())),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getWaitAvg())),
                niceFormat(String.format("%.1f", total.getResponseRate())),
                niceFormat(String.format("%.1f", total.getCallbackResponseRate())),
                niceFormat(String.format("%.1f", total.getSvcLevelAvg())),
                niceFormat(String.format("%.1f", total.getQuantitativeResponseRate())),
                niceFormat(String.format("%.1f", total.getQualitativeResponseRate())),
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
