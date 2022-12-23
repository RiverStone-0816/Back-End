package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.util.PatternUtils;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonFormRequest extends PersonFormUpdateRequest {
	@NotNull("아이디")
	private String id;
	@NotNull("중복체크 여부")
	private Bool isDuplicate = Bool.N;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (Objects.nonNull(isDuplicate))
			if (!isDuplicate.getValue())
				reject(bindingResult, "isDuplicate", "{아이디 중복체크를 진행해 주세요.}");
		if (isNotEmpty(id)) {
			if (!PatternUtils.isPatternId(id))
				reject(bindingResult, "id", "{아이디는 영어와 숫자만 입력 가능합니다.}");
		}

		return super.validate(bindingResult);
	}
}
