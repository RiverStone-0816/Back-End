package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.BillingKind;
import kr.co.eicn.ippbx.model.enums.PrvMemberKind;
import kr.co.eicn.ippbx.model.enums.RidKind;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrvGroupFormRequest extends BaseForm {
    @NotNull("그룹명")
    private String  name;
    @NotNull("프리뷰 유형")
    private Integer prvType;
    @NotNull("상담결과 유형")
    private Integer resultType;
    private String  groupCode;       //그룹코드
    private String  info;        //추가정보

    @NotNull("다이얼시간")
    private Byte dialTimeout = 30;

    /**
     * @see kr.co.eicn.ippbx.model.enums.RidKind
     */
    @NotNull("RID(발신번호) 구분")
    private String ridKind = RidKind.CAMPAIGN.getCode();    // RID(발신번호)설정 구분값
    private String ridData;     //(내선별, 프리뷰그룹별)

    /**
     * @see kr.co.eicn.ippbx.model.enums.BillingKind
     */
    @NotNull("과금번호설정 구분")
    private String billingKind = BillingKind.NUMBER.getCode();  // 과금번호설정 구분
    private String billingData; //(내선별, 프리뷰그룹별 번호)

    /**
     * @see kr.co.eicn.ippbx.model.enums.PrvMemberKind
     */
    @NotNull("상담원 설정")
    private String      memberKind = PrvMemberKind.CAMPAIGN.getCode();  // 상담원설정 구분
    private Set<String> memberDataList;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(ridKind) && ridKind.equals(RidKind.CAMPAIGN.getCode()))
            if (isEmpty(ridData))
                reject(bindingResult, "ridData", "messages.validator.blank", "RID(발신번호)");
            else if (isNotEmpty(ridData) && !NumberUtils.isDigits(ridData))
                reject(bindingResult, "ridData", "messages.validator.invalid", "RID(발신번호)");

        if (isNotEmpty(billingKind)) {
            if (Objects.isNull(EnumUtils.of(BillingKind.class, billingKind)))
                reject(bindingResult, "billingKind", "messages.validator.invalid", "과금번호설정 구분");

            if (!billingKind.equals(BillingKind.PBX.getCode()) && isEmpty(billingData))
                reject(bindingResult, "billingData", "messages.validator.blank", "과금번호");

            if (billingKind.equals(BillingKind.NUMBER.getCode()))
                if (!NumberUtils.isDigits(billingData))
                    reject(bindingResult, "billingData", "messages.validator.invalid", "과금번호");
        }

        if (PrvMemberKind.CAMPAIGN.getCode().equals(memberKind) && CollectionUtils.isEmpty(memberDataList))
            reject(bindingResult, "billingData", "validator.must.select", "상담원");

        return super.validate(bindingResult);
    }
}
