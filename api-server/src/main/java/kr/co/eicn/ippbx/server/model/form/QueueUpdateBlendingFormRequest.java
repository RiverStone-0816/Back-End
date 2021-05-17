package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueueUpdateBlendingFormRequest extends BaseForm {
    // QUEUE_TABLE, QUEUE_MEMBER 테이블에서 찾지 못한 파라미터
    // 블랜딩기능
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.BlendingMode
     */
    private String blendingMode;
    private Integer waitingCount;        //기준고객 대기자수
    private Integer waitingKeepTime;    //기준고객 초과후 유지시간
    private Integer startHour;
    private Integer startMinute;
    private Integer endHour;
    private Integer endMinute;

    private List<String> blendingUser;  //블랜딩 할 상담원
}
