package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonitorQueueListRequest extends BaseForm {
    private String queueName;
    private String summaryView; //요약보기(Y,N)
    private String memberStatusView;   //상담원상태보기(Y,N)
}
