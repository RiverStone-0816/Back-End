package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.ScheduleListKind;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleGroupListFormRequest extends BaseForm {
	@NotNull("스케쥴유형")
	private Integer parent;             // 스케쥴유형 SEQUENCE KEY
	@NotNull("시작시간(분)")
	private Integer fromhour;           // 시작시간 분 (09:00 -> 540)
	@NotNull("종료시간(분)")
	private Integer tohour;             // 종료시간 분 (18:00 -> 1080)
	/**
	 * @see ScheduleListKind
	 */
	@NotNull("유형구분")
	private String  kind;               // 유형구분
	private String  kindData;           // 유형별 입력 데이터

	private Integer firstMentId = 0;    // 첫인사멘트
	private Integer limitNum = 0;       // 비접수 n개이상 초과시 자동멘트송출(0개는 무한대)
	private Integer limitMentId = 0;    // 비접수초과0이상시필수

	private String statYn = "Y";     //  통계반영유무
	private String  worktimeYn = "Y"; // 업무시간 반영 유무

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (Objects.nonNull(fromhour) && Objects.nonNull(tohour)) {
			if (fromhour.compareTo(tohour) >= 0)
				reject(bindingResult, "fromhour", "messages.validator.enddate.after.startdate");
		}
		if (isNotEmpty(kind)) {
			if (ScheduleListKind.AUTO_MENT_REQUEST.getCode().equals(kind))
				if (isEmpty(kindData))
					reject(bindingResult, "kind", "{자동멘트전송시 유형별입력값은 필수 입력사항입니다.}");
				else if (!NumberUtils.isDigits(kindData))
					reject(bindingResult, "kindData", "messages.validator.invalid", kindData);
			else if (ScheduleListKind.BY_SERVICE_GROUP_REQUEST.getCode().equals(kind)) { // 비접수초과 0이상시 필수
				if (limitNum > 0) {
					if (limitMentId < 1)
						reject(bindingResult, "limitMentId", "{비접수 n개 이상초과시 자동멘트송출값은 필수 입력사항입니다.}");
				}
			}
		}

		return super.validate(bindingResult);
	}
}
