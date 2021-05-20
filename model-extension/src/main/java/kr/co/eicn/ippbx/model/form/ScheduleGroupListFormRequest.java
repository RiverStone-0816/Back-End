package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.model.enums.ScheduleKind;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleGroupListFormRequest extends BaseForm {
	@NotNull("스케쥴유형")
	private Integer parent;             // 스케쥴유형 SEQUENCE KEY
	@NotNull("시작시간(분)")
	private Integer fromhour;           // 시작시간 분 (09:00 -> 540)
	@NotNull("종료시간(분)")
	private Integer tohour;             // 종료시간 분 (18:00 -> 1080)
	/**
	 * @see ScheduleKind
	 */
	@NotNull("유형구분")
	private String  kind;               // 유형구분
	private String  kindData;           // 유형별 입력 데이터
	private String kindSoundCode;       // 음원 키

	private String statYn = "Y";     //  통계반영유무
	private String  worktimeYn = "Y"; // 업무시간 반영 유무
	private String  ttsData;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (Objects.nonNull(fromhour) && Objects.nonNull(tohour)) {
			if (fromhour.compareTo(tohour) >= 0)
				reject(bindingResult, "fromhour", "messages.validator.enddate.after.startdate");
		}
		if (isNotEmpty(kind)) {
			if (ScheduleKind.ONY_SOUND_PLAY.getCode().equals(kind) || ScheduleKind.VOICE.getCode().equals(kind)) {
				if (isEmpty(kindSoundCode))
					reject(bindingResult, "kindSoundCode", "{음원은 필수 입력사항입니다.}");
			} else if (ScheduleKind.DIRECT_NUMBER.getCode().equals(kind)) {
				if (isEmpty(kindData))
					reject(bindingResult, "kindData", "{직접연결번호는 필수 입력사항입니다.}");
			} else if (ScheduleKind.CALL_FORWARDING.getCode().equals(kind)) {
				if (isEmpty(kindData))
					reject(bindingResult, "kindData", "{착신전환번호는 필수 입력사항입니다.}");
			} else if (ScheduleKind.IVR_CONNECT.getCode().equals(kind)) {
				if (isEmpty(kindData))
					reject(bindingResult, "kindData", "{IVR은 필수 입력사항입니다.}");
			} else if (ScheduleKind.EXCEPTION_CONTEXT.getCode().equals(kind)) {
				if (isEmpty(kindData))
					reject(bindingResult, "kindData", "{컨텍스트는 필수 입력사항입니다.}");
			}
		}
		if (isNotEmpty(kindSoundCode)) {
			if ("TTS".equals(kindSoundCode) && isEmpty(ttsData)) {
				reject(bindingResult, "ttsData", "{TTS문구를 입력해 주세요.}");
			}
		}

		return super.validate(bindingResult);
	}
}
