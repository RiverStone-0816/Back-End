package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.ApplicationBeanAware;
import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

public class ConsultantCallStatExcel extends AbstractExcel {
    private final List<StatUserResponse<?>> list;
    private final StatUserResponse.UserStat total;
    private final Map<Integer, String> memberStatuses;

    public ConsultantCallStatExcel(List<StatUserResponse<?>> list, StatUserResponse.UserStat total, Map<Integer, String> memberStatuses) {
        this.list = list;
        this.total = total;
        this.memberStatuses = memberStatuses;
        createBody();
    }

    private void createBody() {

        final List<String> headers1 = new ArrayList<>(Arrays.asList("날짜/시간", "부서", "상담원명",
                "총 통화", "",
                "O/B", "", "", "", "", "",
                "I/B", "", "", "", "", "", "",
                "후처리 시간분석", "", ""));
        memberStatuses.forEach((k, v) -> headers1.add(""));
        addRow(sheetHeadStyle, headers1.toArray());

        final List<String> headers2 = new ArrayList<>(Arrays.asList("", "", "",
                "총 건수", "총 시간",
                "총 시도콜", "O/B검수 성공호", "비수신", "O/B 총 통화시간", "O/B 평균통화시간", "통화성공률",
                "O/B 전체콜", "응대호", "I/B 총 통화시간", "I/B 평균통화시간", "평균연결", "포기호", "응대률",
                "후처리건수", "총 후처리시간", "후처리 평균시간"));
        memberStatuses.forEach((k, v) -> headers2.add(v));
        addRow(sheetHeadStyle, headers2.toArray());
        int totalSize = list.stream().mapToInt(e -> e.getUserStatList().size()).reduce(Integer::sum).orElse(0);

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 5, 10));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 11, 17));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 18, 20 + memberStatuses.size()));
        getSheet().addMergedRegion(new CellRangeAddress(totalSize + 2, totalSize + 2, 0, 2));

        final RequestGlobal g = ApplicationBeanAware.requestGlobal();

        for (StatUserResponse<?> e : list) {
            for (StatUserResponse.UserStat userStat : e.getUserStatList()) {
                final List<String> row = new ArrayList<>(Arrays.asList(
                        niceFormat(e.getTimeInformation()),
                        niceFormat(userStat.getGroupName()),
                        niceFormat(userStat.getIdName()),

                        niceFormat(userStat.getTotalCnt()),
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
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getInboundStat().getAvgWaitSec().longValue())),
                        niceFormat(userStat.getInboundStat().getCancel()),
                        niceFormat(userStat.getInboundStat().getAvgRate()),

                        niceFormat(userStat.getMemberStatusStat().getPostProcess()),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getMemberStatusStat().getPostProcessTime())),
                        niceFormat(g.timeFormatFromSecondsWithoutSimpleDateFormat(userStat.getMemberStatusStat().getPostPrecessAvgTime()))
                ));

                memberStatuses.forEach((k, v) -> row.add("" + userStat.getMemberStatusStat().getStatusCountMap().getOrDefault(k.toString(), 0L)));
                addRow(defaultStyle, row.toArray());
            }
        }

        final List<String> row = new ArrayList<>(Arrays.asList(
                "합계", "", "",

                niceFormat(total.getTotalCnt()),
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
        ));
        memberStatuses.forEach((k, v) -> row.add("" + total.getMemberStatusStat().getStatusCountMap().getOrDefault(k.toString(), 0L)));
        addRow(defaultStyle, row.toArray());
    }
}
