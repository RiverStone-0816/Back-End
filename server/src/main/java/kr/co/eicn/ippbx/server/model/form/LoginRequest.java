package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequest extends BaseForm {
	@NotNull("고객사")
	private String company;
	@NotNull("아이디")
	private String id;
	@NotNull("패스워드")
	private String password;
	private String extension;
	private String actionType = "";
	private Boolean isChatLogin;
}
