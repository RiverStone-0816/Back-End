package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.model.dto.customdb.QaResultResponse;
import kr.co.eicn.ippbx.model.dto.customdb.StatQaResultIndividualResponse;

import java.util.*;
import java.util.stream.Collectors;

public class QaResultIndividualStatExcel extends AbstractExcel {
    private final List<StatQaResultIndividualResponse> list;

    public QaResultIndividualStatExcel(List<StatQaResultIndividualResponse> list) {
        this.list = list;
        createBody();
    }

    private void createBody() {
        final Set<String> dates = new TreeSet<>(Comparator.comparing(e -> e));
        for (StatQaResultIndividualResponse e : list)
            for (QaResultResponse e2 : e.getQaResultStat())
                dates.add(e2.getStatDate());

        final Map<StatQaResultIndividualResponse, Map<String, Integer>> codeToDateToCountMap = new HashMap<>();
        for (StatQaResultIndividualResponse e : list)
            codeToDateToCountMap.put(e, e.getQaResultStat().stream().collect(Collectors.toMap(QaResultResponse::getStatDate, QaResultResponse::getCount)));

        final List<String> headers = new ArrayList<>(Collections.singletonList("유형"));
        headers.addAll(dates);
        addRow(sheetHeadStyle, headers.toArray());

        for (StatQaResultIndividualResponse e : list) {
            final List<String> row = new ArrayList<>(Collections.singletonList(e.getCodeName()));
            dates.forEach(date -> row.add(niceFormat(codeToDateToCountMap.get(e).getOrDefault(date, 0))));
            addRow(defaultStyle, row.toArray());
        }
    }
}
