package kr.co.eicn.ippbx.server.util.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

// TODO:
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodValidator.class)
@Documented
public @interface Period {
	String start();

	String end();

	String value() default "필드";

	String message() default "validator.enddate.after.startdate";

	boolean messageSource() default false;

	@SuppressWarnings("UnusedDeclaration") Class<?>[] groups() default {};

	@SuppressWarnings("UnusedDeclaration") Class<? extends Payload>[] payload() default {};
}
