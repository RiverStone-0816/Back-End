package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.*;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class PDSGroupFormRequest extends BaseForm {
    @NotNull("그룹명")
    private String  name;       // 그룹명
    private String  groupCode;  // 소속
    @NotNull("PDS유형")
    private Integer pdsType;    // PDS유형
    @NotNull("실행할 교환기")
    private String  runHost;    // 실행할 교환기
    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupConnectKind
     */
    @NotNull("연결대상 구분")
    private String  connectKind;    // 연결대상 구분
    @NotNull("연결대상")
    private String  connectData;    // 연결대상 정보
    private String  info;           // 추가정보

    @NotNull("다이얼시간")
    private Byte   dialTimeout = 30;   // 다이얼 시간
    @NotNull("녹취")
    private String isRecord;      // 녹취 유무
    @NotNull("콜시도할전화번호필드")
    private String numberField;   // 콜시도할전화번호필드

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupRidKind
     */
    @NotNull("RID(발신번호) 구분")
    private String ridKind = PDSGroupRidKind.CAMPAIGN.getCode(); // RID(발신번호)설정 구분값
    private String ridData;      // RID(발신번호) 정보

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
    private String  speedKind = PDSGroupSpeedKind.MEMBER.getCode();     // 속도 기준
    /**
     * 속도 기준이 대기중상담원기준일 경우
     *
     * @see PDSGroupSpeedMultiple
     */
    private Integer speedData;    // 속도 데이터

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupResultKind
     */
    private String  resultKind = PDSGroupResultKind.RSCH.getCode();      // 상담결과 구분
    private Integer resultType;     // 상담결과유형

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(ridKind) && ridKind.equals(PDSGroupRidKind.CAMPAIGN.getCode()))
            if (isEmpty(ridData))
                reject(bindingResult, "ridData", "messages.validator.blank", "RID(발신번호)");
            else if (isNotEmpty(ridData) && !NumberUtils.isDigits(ridData))
                reject(bindingResult, "ridData", "messages.validator.invalid", "RID(발신번호)");

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

        if (isNotEmpty(resultKind))
            if ("RS".equals(resultKind) && Objects.isNull(resultType))
                reject(bindingResult, "resultType", "messages.validator.blank", "상담결과유형");

        return super.validate(bindingResult);
    }
}
