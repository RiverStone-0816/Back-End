package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationFormRequest extends BaseForm {
	@NotNull("조직명")
	private String groupName;
	private String parentGroupCode;
	@NotNull("조직레벨")
	private Integer groupLevel;
}
