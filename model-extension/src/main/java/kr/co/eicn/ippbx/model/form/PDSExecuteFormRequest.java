package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.PDSGroupBillingKind;
import kr.co.eicn.ippbx.model.enums.PDSGroupRidKind;
import kr.co.eicn.ippbx.model.enums.PDSGroupSpeedKind;
import kr.co.eicn.ippbx.model.enums.PDSGroupSpeedMultiple;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDSExecuteFormRequest extends BaseForm {
    @NotNull("실행명")
    private String executeName;          // 실행명
    @NotNull("실행할 교환기")
    private String runHost;      // 실행할 교환기

    @NotNull("다이얼 시간")
    private Byte   dialTimeout;   // 다이얼 시간
    @NotNull("녹취사용 유무")
    private String isRecord;      // 녹취사용 유무
    @NotNull("콜시도할 전화번호필드")
    private String numberField;   // 콜시도할전화번호필드

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupRidKind
     */
    @NotNull("RID(발신번호) 구분")
    private String ridKind = PDSGroupRidKind.CAMPAIGN.getCode(); // RID(발신번호)설정 구분값
    private String ridData;      // RID(발신번호) 데이터

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupBillingKind
     */
    @NotNull("과금번호설정 구분")
    private String billingKind = PDSGroupBillingKind.NUMBER.getCode();    // 과금번호설정 구분 (PBX:내선별PBX설정, NUMBER:그룹별번호, DIRECT:그룹별직접입력)
    private String billingData;   // 과금번호 정보

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupSpeedKind
     */
    @NotNull("속도 기준")
    private String  speedKind;     // 속도 기준
    /**
     * 속도 기준이 대기중상담원기준일 경우
     *
     * @see PDSGroupSpeedMultiple
     */
    private Integer speedData;    // 속도 데이터

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(ridKind) && ridKind.equals(PDSGroupRidKind.CAMPAIGN.getCode()))
            if (isEmpty(ridData))
                reject(bindingResult, "ridData", "messages.validator.blank", "발신번호");
            else if (isNotEmpty(ridData) && !NumberUtils.isDigits(ridData))
                reject(bindingResult, "ridData", "messages.validator.invalid", "발신번호");

        if (isNotEmpty(billingKind)) {
            if (Objects.isNull(EnumUtils.of(PDSGroupBillingKind.class, billingKind)))
                reject(bindingResult, "billingKind", "messages.validator.invalid", "과금번호설정 구분");

            if (isEmpty(billingData))
                reject(bindingResult, "billingData", "messages.validator.blank", "과금번호");

            if (billingKind.equals(PDSGroupBillingKind.NUMBER.getCode()))
                if (!NumberUtils.isDigits(billingData))
                    reject(bindingResult, "billingData", "messages.validator.invalid", "과금번호");
        }

        if (isNotEmpty(speedKind)) {
            if (Objects.isNull(EnumUtils.of(PDSGroupSpeedKind.class, speedKind)))
                reject(bindingResult, "speedKind", "messages.validator.invalid", speedKind);

            if (Objects.equals(PDSGroupSpeedKind.MEMBER, EnumUtils.of(PDSGroupSpeedKind.class, speedKind))) {
                if (Objects.isNull(EnumUtils.of(PDSGroupSpeedMultiple.class, speedData)))
                    reject(bindingResult, "speedData", "messages.validator.invalid", "속도 배수");
            } else if (Objects.equals(PDSGroupSpeedKind.CHANNEL, EnumUtils.of(PDSGroupSpeedKind.class, speedKind)))
                if (Objects.isNull(speedData))
                    reject(bindingResult, "speedData", "messages.validator.blank", "속도 채널");
        }

        return super.validate(bindingResult);
    }
}
