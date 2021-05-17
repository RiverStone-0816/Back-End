package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.model.entity.eicn.EvaluationResultStatResponse;

import java.util.List;

public class EvaluationStatExcel extends AbstractExcel {
    private final List<EvaluationResultStatResponse> list;

    public EvaluationStatExcel(List<EvaluationResultStatResponse> list) {
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "상담원", "평가지", "평가일", "건수", "총점", "평균");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();

        for (EvaluationResultStatResponse e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getTargetUserName()),
                    niceFormat(e.getEvaluationName()),
                    niceFormat(e.getMaxEvaluationDate()),
                    niceFormat(e.getCnt()),
                    niceFormat(e.getTotal()),
                    niceFormat(e.getAvg())
            );
        }
    }
}
