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
                "총 통화", "",
                "O/B", "", "", "", "",
                "I/B", "", "", "", "",
                "후처리 시간분석", "", "");

        addRow(sheetHeadStyle, "", "", "",
                "총 건수", "총 시간",
                "총 시도콜", "O/B검수 성공호", "비수신", "O/B 총 통화시간", "O/B 평균통화시간", "통화성공률",
                "O/B 전체콜", "응대호", "I/B 총 통화시간", "I/B 평균통화시간", "포기호", "응대률",
                "후처리건수", "총 후처리시간", "후처리 평균시간");

        int totalSize = list.stream().mapToInt(e -> e.getUserStatList().size()).reduce(Integer::sum).orElse(0);

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 5, 10));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 11, 16));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 17, 19));
        getSheet().addMergedRegion(new CellRangeAddress(totalSize + 2, totalSize + 2, 0, 2));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        for (StatUserResponse<?> e : list) {
            for (StatUserResponse.UserStat userStat : e.getUserStatList()) {
                addRow(defaultStyle,
                        niceFormat(e.getTimeInformation()),
                        niceFormat(userStat.getGroupName()),
                        niceFormat(userStat.getIdName()),

                        niceFormat(userStat.getTotalCnt()),
                        niceFormat(g.timeFormatFromSeconds(userStat.getTotalBillSec())),

                        niceFormat(userStat.getOutboundStat().getOutTotal()),
                        niceFormat(userStat.getOutboundStat().getOutSuccess()),
                        niceFormat(userStat.getOutboundStat().getFails()),
                        niceFormat(g.timeFormatFromSeconds(userStat.getOutboundStat().getOutBillSecSum())),
                        niceFormat(g.timeFormatFromSeconds(userStat.getOutboundStat().getAvgBillSec())),
                        niceFormat(userStat.getOutboundStat().getAvgRate()),

                        niceFormat(userStat.getInboundStat().getTotal()),
                        niceFormat(userStat.getInboundStat().getSuccess()),
                        niceFormat(g.timeFormatFromSeconds(userStat.getInboundStat().getBillSecSum())),
                        niceFormat(g.timeFormatFromSeconds(userStat.getInboundStat().getAvgBillSec())),
                        niceFormat(userStat.getInboundStat().getCancel()),
                        niceFormat(userStat.getInboundStat().getAvgRate()),

                        niceFormat(userStat.getMemberStatusStat().getPostProcess()),
                        niceFormat(g.timeFormatFromSeconds(userStat.getMemberStatusStat().getPostProcessTime())),
                        niceFormat(g.timeFormatFromSeconds(userStat.getMemberStatusStat().getPostPrecessAvgTime()))
                );
            }
        }

        addRow(defaultStyle,
                "합계", "", "",

                niceFormat(total.getTotalCnt()),
                niceFormat(g.timeFormatFromSeconds(total.getTotalBillSec())),

                niceFormat(total.getOutboundStat().getOutTotal()),
                niceFormat(total.getOutboundStat().getOutSuccess()),
                niceFormat(total.getOutboundStat().getFails()),
                niceFormat(g.timeFormatFromSeconds(total.getOutboundStat().getOutBillSecSum())),
                niceFormat(g.timeFormatFromSeconds(total.getOutboundStat().getAvgBillSec())),
                niceFormat(String.format("%.1f", total.getOutboundStat().getAvgRate())),

                niceFormat(total.getInboundStat().getTotal()),
                niceFormat(total.getInboundStat().getSuccess()),
                niceFormat(g.timeFormatFromSeconds(total.getInboundStat().getBillSecSum())),
                niceFormat(g.timeFormatFromSeconds(total.getInboundStat().getAvgBillSec())),
                niceFormat(total.getInboundStat().getCancel()),
                niceFormat(String.format("%.1f", total.getInboundStat().getAvgRate())),

                niceFormat(total.getMemberStatusStat().getPostProcess()),
                niceFormat(g.timeFormatFromSeconds(total.getMemberStatusStat().getPostProcessTime())),
                niceFormat(g.timeFormatFromSeconds(total.getMemberStatusStat().getPostPrecessAvgTime()))
        );
    }
}
