package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.statdb.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class InboundCategoryStatExcel extends AbstractExcel {
    private final List<StatCategoryResponse<?>> list;

    public InboundCategoryStatExcel(List<StatCategoryResponse<?>> list) {
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "날짜/시간", "서비스", "IVR",
                "인입경로", "", "",
                "I/B 콜 현황", "", "", "", "",
                "성과지표", "", "", "", "",
                "응대호 대기시간 분석", "", "", "", "",
                "포기호 대기시간 분석", "", "", "", "");
        addRow(sheetHeadStyle, "", "", "",
                "1st", "2nd", "3rd",
                "인입콜수", "단순조회", "연결요청", "응대호", "포기호",
                "평균통화시간", "평균대기시간", "호응답률", "서비스레벨 호응답률", "단순조회율",
                "~10(초)", "~20(초)", "~30(초)", "~40(초)", "40~(초)",
                "~10(초)", "~20(초)", "~30(초)", "~40(초)", "40~(초)");

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 5));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 6, 10));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 11, 15));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 16, 20));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 21, 25));

        final RequestGlobal g = SpringApplicationContextAware.requestGlobal();

        int firstRow = 2;
        int lastRow = 2;
        int recordFirstRow = 2;
        int recordLastRow = 2;
        for (StatCategoryResponse<?> response : list) {
            for (StatCategoryIvrResponse c : response.getRecordList()) {
                for (StatCategoryIvrPathResponse e : c.getRecordNameList()) {
                    addRow(defaultStyle, niceFormat(response.getTimeInformation()), niceFormat(response.getSvcName()), niceFormat(c.getIvrName()),
                            niceFormat(e.getLevel() == 0 ? e.getName() : ""),
                            niceFormat(e.getLevel() == 1 ? e.getName() : ""),
                            niceFormat(e.getLevel() == 2 ? e.getName() : ""),
                            niceFormat(e.getRecord().getTotal()),
                            niceFormat(e.getRecord().getOnlyRead()),
                            niceFormat(e.getRecord().getConnReq()),
                            niceFormat(e.getRecord().getSuccess()),
                            niceFormat(e.getRecord().getCancel()),

                            niceFormat(g.timeFormatFromSeconds(e.getRecord().getBillSecAvg())),
                            niceFormat(g.timeFormatFromSeconds(e.getRecord().getWaitAvg())),
                            niceFormat(e.getRecord().getResponseRate()),
                            niceFormat(e.getRecord().getSvcLevelAvg()),
                            niceFormat(e.getRecord().getIvrAvg()),

                            niceFormat(e.getRecord().getWaitSucc_0_10()),
                            niceFormat(e.getRecord().getWaitSucc_10_20()),
                            niceFormat(e.getRecord().getWaitSucc_20_30()),
                            niceFormat(e.getRecord().getWaitSucc_30_40()),
                            niceFormat(e.getRecord().getWaitSucc_40()),

                            niceFormat(e.getRecord().getWaitCancel_0_10()),
                            niceFormat(e.getRecord().getWaitCancel_10_20()),
                            niceFormat(e.getRecord().getWaitCancel_20_30()),
                            niceFormat(e.getRecord().getWaitCancel_30_40()),
                            niceFormat(e.getRecord().getWaitCancel_40()));
                }
                lastRow += c.getRecordNameList().size();
                recordLastRow += c.getRecordNameList().size();
                getSheet().addMergedRegion(new CellRangeAddress(recordFirstRow, recordLastRow, 2, 2));
                recordFirstRow = recordLastRow + 1;
            }
            getSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
            getSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, 1, 1));
            firstRow = lastRow + 1;
        }
    }
}
