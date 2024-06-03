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
public class DayScheduleInfoFormDeleteRequest extends BaseForm {
	@NotNull("삭제 기준일")
	private String deleteDate;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(deleteDate))
			if (!PatternUtils.isDate(deleteDate))
				reject(bindingResult, "fromDate", "messages.validator.pattern", "삭제 기준일");

		return super.validate(bindingResult);
	}
}
