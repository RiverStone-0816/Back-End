package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.PDSGroupBillingKind;
import kr.co.eicn.ippbx.server.model.enums.PDSGroupSpeedKind;
import kr.co.eicn.ippbx.server.model.enums.PDSGroupSpeedMultiple;
import kr.co.eicn.ippbx.server.util.EnumUtils;
import kr.co.eicn.ippbx.server.util.valid.Length;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDSExecuteFormRequest extends BaseForm {
	@NotNull("실행명")
	private String executeName;          // 실행명
	@NotNull("실행할 교환기")
	private String    runHost;      // 실행할 교환기
	@NotNull("다이얼 시간")
	private Byte      dialTimeout;   // 다이얼 시간
	@NotNull("녹취사용 유무")
	private String    isRecord;      // 녹취사용 유무
	@NotNull("콜시도할 전화번호필드")
	private String    numberField;   // 콜시도할전화번호필드
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.PDSGroupRidKind
	 */
	@NotNull("RID(발신번호) 구분")
	private String ridKind; // RID(발신번호)설정 구분값
	@NotNull("RID(발신번호)")
	private String    ridData;      // RID(발신번호) 데이터
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.PDSGroupBillingKind
	 */
	@NotNull("과금번호설정 구분")
	private String    billingKind;    // 과금번호설정 구분
	@NotNull("과금번호")
	@Length(value = "과금번호", min = 9, max = 11)
	private String    billingData;   // 과금번호 데이터
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.PDSGroupSpeedKind
	 */
	@NotNull("PDS속도 기준")
	private String    speedKind;     // PDS속도 기준
	/**
	 * PDS속도 기준이 대기중상담원기준일 경우
	 * @see PDSGroupSpeedMultiple
	 */
	@NotNull("PDS속도")
	private Integer   speedData;    // PDS속도 데이터

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(ridData))
			if (!NumberUtils.isDigits(ridData))
				reject(bindingResult, "ridData", "messages.validator.invalid", ridData);
		if (isNotEmpty(billingKind))
			if (Objects.isNull(EnumUtils.of(PDSGroupBillingKind.class, billingKind)))
				reject(bindingResult, "billingKind", "messages.validator.invalid", billingKind);
		if (isNotEmpty(speedKind))
			if (Objects.isNull(EnumUtils.of(PDSGroupSpeedKind.class, speedKind)))
				reject(bindingResult, "speedKind", "messages.validator.invalid", speedKind);
			else if (Objects.equals(PDSGroupSpeedKind.STANDBY_CONSULTATION_BASE, EnumUtils.of(PDSGroupSpeedKind.class, speedKind)))
				if (Objects.isNull(EnumUtils.of(PDSGroupSpeedMultiple.class, speedData)))
					reject(bindingResult, "speedData", "messages.validator.invalid", speedData);

		return super.validate(bindingResult);
	}
}
