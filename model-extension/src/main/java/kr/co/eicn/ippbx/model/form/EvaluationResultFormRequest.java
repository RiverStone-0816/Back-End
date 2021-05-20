package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationResultFormRequest extends BaseForm {
	@NotNull("평가지 아이디")
	private Long evaluationId;
	@NotNull("녹취통화이력 아이디")
	private Integer cdrId;
	@NotNull("평가대상")
	private String targetUserid; // 평가대상아이디
	private String memo;
	private boolean resultTransfer = false;

	@Valid
	private List<EvaluationItemScoreFormRequest> scores;

	@Override
	public boolean validate(BindingResult bindingResult) {
		return super.validate(bindingResult);
	}
}
