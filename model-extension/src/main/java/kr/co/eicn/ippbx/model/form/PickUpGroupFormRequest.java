package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PickUpGroupFormRequest extends BaseForm {
	@NotNull("당겨받기명")
	private String  groupname;
	private String  host;
	@NotNull("조직코드")
	private String groupCode;
}
