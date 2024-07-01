package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.PatternUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class DayScheduleInfoUpdateFormRequest extends BaseForm {
	private String groupCode; // 조직코드
	@NotNull("스케쥴유형")
	private Integer groupId; // 스케쥴유형
	@NotNull("시작일")
	private String fromDate; // 시작일(YYYY-MM-DD)

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(fromDate))
			if (!PatternUtils.isDate(fromDate))
				reject(bindingResult, "fromDate", "messages.validator.pattern", "시작일");

		return super.validate(bindingResult);
	}
}
