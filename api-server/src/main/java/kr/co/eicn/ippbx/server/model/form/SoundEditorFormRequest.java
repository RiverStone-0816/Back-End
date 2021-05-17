package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.PatternUtils;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

@EqualsAndHashCode(callSuper = true)
@Data
public class SoundEditorFormRequest extends BaseForm {
	@NotNull("재생속도")
	private Integer playSpeed;
	@NotNull("음원명/컬러링명")
	private String soundName;
	@NotNull("파일명")
	private String fileName;

	private String comment; // 음원

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (StringUtils.isNotEmpty(fileName))
			if (PatternUtils.isHangule(fileName) || !PatternUtils.isValidFileName(fileName))
				reject(bindingResult, "fileName", "messages.validator.invalid", fileName);
		return super.validate(bindingResult);
	}
}
