package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.PatternUtils;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordEncKeyFormRequest extends BaseForm {
	@NotNull("암호키 적용시간")
	private String applyDate;           // yyyy-MM-dd HH:mm:ss
	@NotNull("암호키")
	@Size(min = 6, max = 99)
	private String encKey;

	@Override
	public boolean validate(BindingResult bindingResult) {
		if (isNotEmpty(applyDate)) {
			if (!PatternUtils.isDateTime(applyDate))
				reject(bindingResult, "applyDate", "{암호키 적용시간을 형식에 맞게 입력하여 주세요.}", "");
			else if (LocalDateTime.now().isAfter(LocalDateTime.parse(applyDate, DateTimeFormatter.ofPattern(Constants.DEFAULT_DATETIME_PATTERN))))
				reject(bindingResult, "applyDate", "적용 시간은 현재 시간 이후로 설정해주세요.", "");
		}
		if (isNotEmpty(encKey))
			if (!PatternUtils.isEncKey(encKey))
				reject(bindingResult, "encKey", "{암호키는 영문과 숫자 조합으로 설정해주세요.}", "");

		return super.validate(bindingResult);
	}
}
