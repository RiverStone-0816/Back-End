package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.model.enums.Button;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class IvrButtonMappingFormRequest extends BaseForm {
	// update시 버튼 sequence key
	private Integer seq;
	@NotNull("IVR명")
	private String name;
	/**
	 * @see Button
	 */
	@NotNull("버튼과 매핑되는 정보")
	private String button;

	public boolean validate(String prefix, BindingResult bindingResult) {
		if (isNotEmpty(button)) {
			if (Objects.isNull(Button.of(button))) {
				reject(bindingResult, prefix + "button", "messages.validator.invalid", button);
			}
		}

		return super.validate(bindingResult);
	}
}
