package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.ApplicationBeanAware;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryIvrPathResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryIvrResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InboundCategoryStatExcel extends AbstractExcel {
    private final List<StatCategoryResponse<?>> list;
    private final Integer maxLevel;

    public InboundCategoryStatExcel(List<StatCategoryResponse<?>> list, Integer maxLevel) {
        this.list = list;
        this.maxLevel = maxLevel;
        createBody();
    }

    private void createBody() {
        final List<String> headers1 = new ArrayList<>(Arrays.asList("날짜/시간", "서비스", "IVR", "인입경로"));
        for (int i = 0; i < maxLevel; i++) {
            headers1.add("");
        }
        headers1.addAll(Arrays.asList(
                "I/B 콜 현황", "", "", "", "",
                "성과지표", "", "", "", "",
                "응대호 대기시간 분석", "", "", "", "",
                "포기호 대기시간 분석", "", "", "", ""));
        addRow(sheetHeadStyle, headers1.toArray());

        final List<String> headers2 = new ArrayList<>(Arrays.asList("", "", "", "1st"));
        for (int i = 2; i < maxLevel + 2; i++) {
            if (i == 2)
                headers2.add(i + "nd");
            else if (i == 3)
                headers2.add(i + "rd");
            else
                headers2.add(i + "th");

        }
        headers2.addAll(Arrays.asList(
                "인입콜수", "단순조회", "연결요청", "응대호", "포기호",
                "평균통화시간", "평균대기시간", "호응답률", "서비스레벨 호응답률", "단순조회율",
                "~10(초)", "~20(초)", "~30(초)", "~40(초)", "40~(초)",
                "~10(초)", "~20(초)", "~30(초)", "~40(초)", "40~(초)"
        ));
        addRow(sheetHeadStyle, headers2.toArray());


        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        if (maxLevel > 0) {
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 3 + maxLevel));
        }

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 4 + maxLevel, 8 + maxLevel));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 9 + maxLevel, 13 + maxLevel));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 14 + maxLevel, 18 + maxLevel));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 19 + maxLevel, 23 + maxLevel));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        int firstRow = 2;
        int lastRow = 1;
        int recordFirstRow = 2;
        int recordLastRow = 1;
        for (StatCategoryResponse<?> response : list) {
            for (StatCategoryIvrResponse c : response.getRecordList()) {
                for (StatCategoryIvrPathResponse e : c.getRecordNameList()) {
                    List<Object> row = new ArrayList<>(Arrays.asList(niceFormat(response.getTimeInformation()), niceFormat(response.getSvcName()), niceFormat(c.getIvrName())));
                    for (int i = 0; i <= maxLevel; i++) {
                        if (e.getLevel() == i)
                            row.add(niceFormat(e.getName()));
                        else
                            row.add("");
                    }
                    row.addAll(Arrays.asList(
                            niceFormat(e.getRecord().getTotal()),
                            niceFormat(e.getRecord().getOnlyRead()),
                            niceFormat(e.getRecord().getConnReq()),
                            niceFormat(e.getRecord().getSuccess()),
                            niceFormat(e.getRecord().getCancel()),

                            niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(e.getRecord().getBillSecAvg())),
                            niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(e.getRecord().getWaitAvg())),
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
                            niceFormat(e.getRecord().getWaitCancel_40())
                    ));
                    addRow(defaultStyle, row);
                }

                if (c.getRecordNameList().size() == 0) {
                    List<Object> row = new ArrayList<>(Arrays.asList(niceFormat(response.getTimeInformation()), niceFormat(response.getSvcName()), niceFormat(c.getIvrName())));
                    for (int i = 0; i < 21 + maxLevel; i++)
                        row.add("");
                    addRow(defaultStyle, row);
                }

                lastRow += c.getRecordNameList().size() != 0 ? c.getRecordNameList().size() : 1;
                recordLastRow += c.getRecordNameList().size() != 0 ? c.getRecordNameList().size() : 1;
                if (recordFirstRow != recordLastRow) {
                    getSheet().addMergedRegion(new CellRangeAddress(recordFirstRow, recordLastRow, 2, 2));
                }
                recordFirstRow = recordLastRow + 1;
            }

            if (response.getRecordList().size() == 0) {
                List<Object> row = new ArrayList<>(Arrays.asList(niceFormat(response.getTimeInformation()), niceFormat(response.getSvcName())));
                for (int i = 0; i < 22 + maxLevel; i++)
                    row.add("");
                addRow(defaultStyle, row);
            }

            if (firstRow != lastRow && response.getRecordList().size() != 0) {
                getSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
                getSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, 1, 1));
            }
            firstRow = lastRow + 1;
        }
    }
}
