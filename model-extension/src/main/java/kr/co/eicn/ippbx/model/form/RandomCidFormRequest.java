package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.Length;
import kr.co.eicn.ippbx.util.valid.NotNull;
import kr.co.eicn.ippbx.util.valid.Range;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class RandomCidFormRequest extends BaseForm {
	@NotNull("발신번호")
	@Length(min = 8, max = 11)
	private String  number;
	private String  groupCode;
	@NotNull("단축번호")
	@Range(min = 1, max = 9)
	private Byte    shortNum;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(number))
			if (!NumberUtils.isDigits(number))
				reject(bindingResult, "number", "messages.validator.invalid", number);
		return super.validate(bindingResult);
	}
}
