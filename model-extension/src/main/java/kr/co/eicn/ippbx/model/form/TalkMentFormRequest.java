package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.Length;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkMentFormRequest extends BaseForm {
	@NotNull("멘트명")
	private String  mentName;
	@NotNull("멘트")
	@Length(min = 1, max = 1000)
	private String  ment;
}
