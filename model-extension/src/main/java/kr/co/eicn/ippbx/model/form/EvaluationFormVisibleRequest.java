package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationFormVisibleRequest extends BaseForm {
	@NotNull("아이디")
	private Long                  id;
	@NotNull("숨김처리여부")
	private String                hidden;
}
