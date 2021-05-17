package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.EncType;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordEncFormRequest extends BaseForm {
	/**
	 * @see EncType
	 */
	@NotNull("암호화 방식")
	private String  encType;        // 암호화 방식

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(encType))
			if (Objects.isNull(EncType.of(encType)))
				reject(bindingResult, "encType", "", "지원되지 않는 암호화 타입입니다.");

		return super.validate(bindingResult);
	}
}
