package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.model.dto.customdb.QaResultResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultCodeResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultFieldResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultResponse;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;
import java.util.stream.Collectors;

public class QaResultLinkStatExcel extends AbstractExcel {
    private final List<StatQaResultResponse> list;

    public QaResultLinkStatExcel(List<StatQaResultResponse> list) {
        this.list = list;
        createBody();
    }

    private void createBody() {
        final Set<String> dates = new TreeSet<>(Comparator.comparing(e -> e));
        for (StatQaResultResponse e : list)
            for (StatQaResultFieldResponse e2 : e.getFieldResponses())
                for (StatQaResultCodeResponse e3 : e2.getCodeResponses())
                    for (QaResultResponse e4 : e3.getQaResultStat())
                        dates.add(e4.getStatDate());

        final Map<String, Map<String, Integer>> codeToDateToCountMap = new HashMap<>();
        for (StatQaResultResponse e : list)
            for (StatQaResultFieldResponse e2 : e.getFieldResponses())
                for (StatQaResultCodeResponse e3 : e2.getCodeResponses())
                    codeToDateToCountMap.put(e3.getCodeId(), e3.getQaResultStat().stream().collect(Collectors.toMap(QaResultResponse::getStatDate, QaResultResponse::getCount)));

        final List<String> headers = new ArrayList<>(Arrays.asList("고객유형", "업무구분", "세부구분"));
        headers.addAll(dates);
        addRow(sheetHeadStyle, headers.toArray());

        int nRow = 1;
        for (StatQaResultResponse e : list) {
            if (e.getFieldResponses().isEmpty()) continue;

            final int e1StartRowNumber = nRow;
            for (StatQaResultFieldResponse e2 : e.getFieldResponses()) {
                if (e2.getCodeResponses().isEmpty()) continue;

                final int e2StartRowNumber = nRow;
                for (StatQaResultCodeResponse e3 : e2.getCodeResponses()) {
                    nRow++;
                    final List<String> row = new ArrayList<>(Arrays.asList(e.getName(), e2.getFieldName(), e3.getCodeName()));
                    dates.forEach(date -> row.add(niceFormat(codeToDateToCountMap.get(e3.getCodeId()).getOrDefault(date, 0))));
                    addRow(defaultStyle, row.toArray());
                }
                final int e2EndRowNumber = nRow;
                if (e2StartRowNumber > e2EndRowNumber - 1)
                    getSheet().addMergedRegion(new CellRangeAddress(e2StartRowNumber, e2EndRowNumber - 1, 1, 1));
            }
            final int e1EndRowNumber = nRow;
            if (e1StartRowNumber > e1EndRowNumber - 1)
                getSheet().addMergedRegion(new CellRangeAddress(e1StartRowNumber, e1EndRowNumber - 1, 0, 0));
        }
    }
}
