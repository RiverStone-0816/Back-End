package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationResultEntity;

import java.util.List;

public class EvaluationResultExcel extends AbstractExcel {
    private final List<EvaluationResultEntity> list;

    public EvaluationResultExcel(List<EvaluationResultEntity> list) {
        this.list = list;
        createBody();
    }

    private void createBody() {
        addRow(sheetHeadStyle, "상담원", "수신번호", "발신번호", "통화시간", "평가지", "평가시간", "점수", "진행상태");

        final RequestMessage message = SpringApplicationContextAware.requestMessage();

        for (EvaluationResultEntity e : list) {
            addRow(defaultStyle,
                    niceFormat(e.getTargetUserName()),
                    niceFormat(e.getCdr().getDst()),
                    niceFormat(e.getCdr().getSrc()),
                    niceFormat(e.getCdr().getRingDate()),
                    niceFormat(e.getForm().getName()),
                    niceFormat(e.getEvaluationDate()),
                    niceFormat(e.getTotalScore()),
                    niceFormat(message.getEnumText(e.getProcessStatus()))
            );
        }
    }
}
