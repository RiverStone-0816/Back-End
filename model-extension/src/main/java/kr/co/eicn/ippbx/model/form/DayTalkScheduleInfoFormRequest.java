package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.PatternUtils;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class DayTalkScheduleInfoFormRequest extends TalkScheduleInfoFormRequest {
	@NotNull("시작일")
	private String fromDate;
	@NotNull("종료일")
	private String toDate;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(fromDate) && isNotEmpty(toDate))
			if (!PatternUtils.isDate(fromDate))
				reject(bindingResult, "fromDate", "messages.validator.pattern", "시작일");
			else if (!PatternUtils.isDate(toDate))
				reject(bindingResult, "toDate", "messages.validator.pattern", "종료일");
			else if (LocalDate.parse(fromDate).isAfter(LocalDate.parse(toDate)))
				reject(bindingResult, "fromDate", "{시작시간이 종료시간보다 이전이어야 합니다.}");

		return super.validate(bindingResult);
	}
}
