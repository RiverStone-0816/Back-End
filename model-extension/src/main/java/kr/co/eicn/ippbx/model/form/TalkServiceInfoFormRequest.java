package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.containsWhitespace;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkServiceInfoFormRequest extends BaseForm {
	@NotNull("상담톡 서비스명")
	private String  kakaoServiceName;     //  상담톡 서비스명
	@NotNull("상담톡 아이디")
	private String  kakaoServiceId;        // 상담톡아이디
	@NotNull("상담톡키")
	private String  senderKey;            // 상담톡키
	@NotNull("상담톡 활성화")
	private String  isChattEnable;        // 상담창활성화

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(senderKey))
			if (containsWhitespace(senderKey))
				reject(bindingResult, "senderKey", "{빈 공백문자를 포함할 수 없습니다.}", "");
		if (isNotEmpty(kakaoServiceId))
			if (containsWhitespace(kakaoServiceId))
				reject(bindingResult, "kakaoServiceId", "{빈 공백문자를 포함할 수 없습니다.}", "");

		return super.validate(bindingResult);
	}
}
