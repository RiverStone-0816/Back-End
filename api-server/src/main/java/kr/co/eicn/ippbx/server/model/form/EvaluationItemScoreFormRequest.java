package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationItemScoreFormRequest extends BaseForm {
	@NotNull("평가항목 아이디")
	private Long      itemId;
	@NotNull("평가점수")
	private Byte      score;
}
