package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.PatternUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonPasswordUpdateRequest extends BaseForm {
	@NotNull("비밀번호")
	private String password;
	@NotNull("비밀번호 확인")
	private String passwordConfirm;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(password)) {
			if (!PatternUtils.isPasswordValid(password))
				reject(bindingResult, "password", "{비밀번호는 문자, 숫자, 특수문자의 조합으로 9~20자리로 입력해주세요.}");
			if (PatternUtils.isPasswordSameCharacter(password))
				reject(bindingResult, "password", "{동일문자를 3번 이상 사용할 수 없습니다.}");
			if (PatternUtils.isPasswordContinuousCharacter(password))
				reject(bindingResult, "password", "{연속된 문자열(123 또는 321, abc, cba 등)을 3자 이상 사용 할 수 없습니다.}");
			if (!Objects.equals(password, passwordConfirm))
				reject(bindingResult, "password", "validator.equal", "비밀번호", "비밀번호 확인");
		}

		return super.validate(bindingResult);
	}
}
