package kr.co.eicn.ippbx.server;

import kr.co.eicn.ippbx.server.model.form.IvrFormRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Validation;
import javax.validation.Validator;

@Slf4j
@SpringBootTest
public class ValidationFormUnitTest {
	private Validator validator;

	@BeforeEach
	public void setupValidatorInstance() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

//	@Test
	public void whenValidation_thenConstraintViolationByType() {
		final IvrFormRequest form = new IvrFormRequest();
		form.setName("xxxxxx");

//		final Set<ConstraintViolation<IvrFormRequest>> validate = validator.validate(form, IvrTreeGroups.type1.class);

//		validate.forEach(action -> {
//			log.info("message:{}", action.getMessage());
//		});
	}
}
