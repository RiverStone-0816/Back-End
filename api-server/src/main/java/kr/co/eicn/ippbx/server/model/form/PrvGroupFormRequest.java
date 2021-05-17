package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class PrvGroupFormRequest extends BaseForm {
    @NotNull("그룹명")
    private String name;
    @NotNull("프리뷰 유형")
    private Integer prvType;
    @NotNull("상담결과 유형")
    private Integer resultType;
    private String groupCode;       //그룹코드
    private String info;        //추가정보

    @NotNull("다이얼시간")
    private Byte dialTimeout;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.RidKind
     */
    @NotNull("발신번호 설정")
    private String ridKind;
    private String ridData;     //(내선별, 프리뷰그룹별)

    /**
     * @see kr.co.eicn.ippbx.server.model.enums.BillingKind
     */
    @NotNull("과금번호 설정")
    private String billingKind;
    private String billingData; //(내선별, 프리뷰그룹별 번호)

    /**
     * @see kr.co.eicn.ippbx.server.model.enums.PrvMemberKind
     */
    @NotNull("상담원 설정")
    private String memberKind;
    private Set<String> memberDataList;
}
