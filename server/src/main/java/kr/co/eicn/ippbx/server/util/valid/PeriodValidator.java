package kr.co.eicn.ippbx.server.util.valid;

import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Objects;

// TODO:
public class PeriodValidator implements ConstraintValidator<Period, Object> {
	private static final Logger logger = LoggerFactory.getLogger(PeriodValidator.class);

	private String start;
	private String end;
	private String field;
	private String message;
	private boolean useMessageSource;

	@Override
	public void initialize(Period constraintAnnotation) {
		start = constraintAnnotation.start();
		end = constraintAnnotation.end();
		field = constraintAnnotation.value();
		message = constraintAnnotation.message();
		useMessageSource = constraintAnnotation.messageSource();
	}

	@Override
	public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
		if (o == null)
			return true;

		logger.warn("object {}", o);

		logger.warn("start -> {}", start);

		Class<?> aClass = o.getClass();

		Field startDate = ReflectionUtils.findField(aClass, start);
		Field endDate = ReflectionUtils.findField(aClass, end);

		if (startDate != null && endDate != null) {
			logger.warn("startDate {}", startDate);
			logger.warn("endDate{}", endDate);

			logger.warn("type {}", startDate.getType());

			if (Objects.equals(startDate.getType(), Date.class) && Objects.equals(endDate.getType(), Date.class)) {
				logger.warn("startDate {},  endDate {}", startDate.getType(), endDate.getType());

				Date startValue = (Date) ReflectionUtils.getField(startDate, o);
				Date endValue = (Date) ReflectionUtils.getField(startDate, o);

				logger.warn("start value {}", startValue);
				logger.warn("end value {}", endValue);
			}
		}
		return false;
	}

	private void error(ConstraintValidatorContext constraintValidatorContext) {
		final RequestMessage requestMessage = SpringApplicationContextAware.requestMessage();
		final String message = requestMessage.getText(this.message, useMessageSource ? requestMessage.getText(field) : field, start, end);
		constraintValidatorContext.disableDefaultConstraintViolation();
		constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
	}
}
