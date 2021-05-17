package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class PickUpGroupFormUpdateRequest extends BaseForm {
	@NotNull("당겨받기명")
	private String  groupname;
	private Set<String> peers;

	private String host = "localhost";
}
