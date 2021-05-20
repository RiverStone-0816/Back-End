package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import kr.co.eicn.ippbx.util.valid.Range;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationItemFormRequest extends BaseForm {
	@NotNull("항목명")
	private String  name;
	@NotNull("평가기준")
	private String  valuationBasis;
	@NotNull("배점")
	@Range(min= -1, max = 101)
	private Integer maxScore;
	private String  remark;
}
