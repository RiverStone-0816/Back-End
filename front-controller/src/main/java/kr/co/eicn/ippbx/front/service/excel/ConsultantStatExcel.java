package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.ApplicationBeanAware;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class ConsultantStatExcel extends AbstractExcel {
    private final List<StatUserResponse<?>> list;
    private final StatUserResponse.UserStat total;

    public ConsultantStatExcel(List<StatUserResponse<?>> list, StatUserResponse.UserStat total) {
        this.list = list;
        this.total = total;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "날짜/시간", "부서", "상담원명",
                "총 통화", "", "",
                "O/B", "", "", "", "", "",
                "I/B", "", "", "", "", "", "",
                "후처리 시간분석", "", "");

        addRow(sheetHeadStyle, "", "", "",
                "전체건수", "통화건수", "총 시간",
                "총 시도콜", "성공호", "비수신", "총 통화시간", "평균통화시간", "통화성공률",
                "전체콜", "응대호", "총 통화시간", "평균통화시간", "평균대기시간", "개인비수신", "응대률",
                "후처리건수", "총 후처리시간", "후처리 평균시간");

        int totalSize = list.stream().mapToInt(e -> e.getUserStatList().size()).reduce(Integer::sum).orElse(0);

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 5));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 6, 11));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 12, 18));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 19, 21));
        getSheet().addMergedRegion(new CellRangeAddress(totalSize + 2, totalSize + 2, 0, 2));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        int firstRow = 2;
        int lastRow = 1;
        for (StatUserResponse<?> e : list) {
            for (StatUserResponse.UserStat userStat : e.getUserStatList()) {
                addRow(defaultStyle,
                        niceFormat(e.getTimeInformation()),
                        niceFormat(userStat.getGroupName()),
                        niceFormat(userStat.getIdName()),

                        niceFormat(userStat.getTotalCnt()),
                        niceFormat(userStat.getTotalSuccess()),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getTotalBillSec())),

                        niceFormat(userStat.getOutboundStat().getOutTotal()),
                        niceFormat(userStat.getOutboundStat().getOutSuccess()),
                        niceFormat(userStat.getOutboundStat().getFails()),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getOutboundStat().getOutBillSecSum())),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getOutboundStat().getAvgBillSec())),
                        niceFormat(userStat.getOutboundStat().getAvgRate()),

                        niceFormat(userStat.getInboundStat().getTotal()),
                        niceFormat(userStat.getInboundStat().getSuccess()),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getInboundStat().getBillSecSum())),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getInboundStat().getAvgBillSec())),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getInboundStat().getAvgWaitSec())),
                        niceFormat(userStat.getInboundStat().getCancel()),
                        niceFormat(userStat.getInboundStat().getAvgRate()),

                        niceFormat(userStat.getMemberStatusStat().getPostProcess()),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getMemberStatusStat().getPostProcessTime())),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getMemberStatusStat().getPostPrecessAvgTime()))
                );
            }
            lastRow += e.getUserStatList().size();
            if(firstRow != lastRow){
                getSheet().addMergedRegion(new CellRangeAddress(firstRow, lastRow, 0, 0));
            }
            firstRow = lastRow + 1;
        }

        addRow(defaultStyle,
                "합계", "", "",

                niceFormat(total.getTotalCnt()),
                niceFormat(total.getTotalSuccess()),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getTotalBillSec())),

                niceFormat(total.getOutboundStat().getOutTotal()),
                niceFormat(total.getOutboundStat().getOutSuccess()),
                niceFormat(total.getOutboundStat().getFails()),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getOutboundStat().getOutBillSecSum())),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getOutboundStat().getAvgBillSec())),
                niceFormat(String.format("%.1f", total.getOutboundStat().getAvgRate())),

                niceFormat(total.getInboundStat().getTotal()),
                niceFormat(total.getInboundStat().getSuccess()),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getInboundStat().getBillSecSum())),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getInboundStat().getAvgBillSec())),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getInboundStat().getAvgWaitSec())),
                niceFormat(total.getInboundStat().getCancel()),
                niceFormat(String.format("%.1f", total.getInboundStat().getAvgRate())),

                niceFormat(total.getMemberStatusStat().getPostProcess()),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getMemberStatusStat().getPostProcessTime())),
                niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(total.getMemberStatusStat().getPostPrecessAvgTime()))
        );
    }
}
