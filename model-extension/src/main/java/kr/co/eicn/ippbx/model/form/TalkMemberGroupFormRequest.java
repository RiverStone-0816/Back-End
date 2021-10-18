package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.TalkMemberDistributionType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkMemberGroupFormRequest extends BaseForm {
    @NotNull("상담톡그룹명")
    private String groupName;
    @NotNull("추가 사용자")
    private List<String> personIds; // 추가 사용자

    private TalkMemberDistributionType distributionPolicy;                // 분배정책
    private String initMent;                    // 인사멘트
    private Integer autoWarnMin = 0;                // 자동종료안내시간
    private String autoWarnMent;                // 자동중료안내맨트
    private Integer autoExpireMin = 0;                // 자동종료시간
    private String autoExpireMent;                // 자동종료멘트
    private Integer unassignCnt = 0;                // 비접수 건수
    private String unassignMent;                // 비접수 초과시 멘트
    private Integer memberUnanswerMin = 0;            // 상담원 무응답시간
    private String memberUnanswerMent;            // 상담원 무응답시 멘트
}
