package kr.co.eicn.ippbx.server.util.spring;

import kr.co.eicn.ippbx.server.util.StringUtils;
import kr.co.eicn.ippbx.server.util.spring.SpringApplicationContextAware;
import org.springframework.validation.BindingResult;

public abstract class BaseForm {
	public static void reject(BindingResult result, String field, String message, Object... objects) {
		if (!(message.startsWith("{") && message.endsWith("}"))) {
			message = SpringApplicationContextAware.requestMessage().getText(message, objects);
		} else {
			message = StringUtils.slice(message, 1, -1);
		}
		result.rejectValue(field, null, message);
	}

	public boolean validate(BindingResult bindingResult) {
		return !bindingResult.hasErrors();
	}
}