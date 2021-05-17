package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationCategoryFormRequest extends BaseForm {
	@NotNull("분류명")
	private String name;
	@Valid
	private List<EvaluationItemFormRequest> items;
}
