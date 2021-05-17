package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatHuntInboundResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatHuntResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class HuntStatExcel extends AbstractExcel {
    private final List<StatHuntResponse<?>> list;
    private final StatHuntInboundResponse total;

    public HuntStatExcel(List<StatHuntResponse<?>> list, StatHuntInboundResponse total) {
        this.list = list;
        this.total = total;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle,
                "날짜/시간",
                "큐",
                "I/B", "", "", "", "", "", "", "");
        addRow(sheetHeadStyle,
                "",
                "",
                "I/B 연결요청", "응대호", "포기호", "콜백", "I/B 총통화시간", "평균통화시간", "응답률", "서비스레벨 호응답률");

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 2, 9));

        final int recordCount = list.stream().mapToInt(e -> e.getStatQueueInboundResponses().size()).sum();
        if (recordCount > 0)
            getSheet().addMergedRegion(new CellRangeAddress(recordCount + 1, recordCount + 1, 0, 1));

        final RequestGlobal g = SpringApplicationContextAware.requestGlobal();

        for (StatHuntResponse<?> element : list) {
            for (StatHuntInboundResponse e : element.getStatQueueInboundResponses()) {
                addRow(defaultStyle,
                        niceFormat(element.getTimeInformation()),
                        niceFormat(e.getQueueName()),

                        niceFormat(e.getInTotal()),
                        niceFormat(e.getInSuccess()),
                        niceFormat(e.getCancel()),
                        niceFormat(e.getCallbackCount()),
                        niceFormat(g.timeFormatFromSeconds(e.getInBillSecSum())),
                        niceFormat(g.timeFormatFromSeconds(e.getAvgBillSec())),
                        niceFormat(e.getAvgRateValue()),
                        niceFormat(e.getServiceLevelOk())
                );
            }
        }

        if (recordCount > 0)
            addRow(defaultStyle,
                    "합계",
                    "",

                    niceFormat(total.getInTotal()),
                    niceFormat(total.getInSuccess()),
                    niceFormat(total.getCancel()),
                    niceFormat(total.getCallbackCount()),
                    niceFormat(g.timeFormatFromSeconds(total.getInBillSecSum())),
                    niceFormat(g.timeFormatFromSeconds(total.getAvgBillSec())),
                    niceFormat(String.format("%.1f", total.getAvgRateValue())),
                    niceFormat(total.getServiceLevelOk())
            );
    }
}
