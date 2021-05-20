package kr.co.eicn.ippbx.model.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CompanyTreeNameFormRequest {
	@NotNull
	private Integer groupLevel;
	@NotNull
	private String  groupTreeName;
}
