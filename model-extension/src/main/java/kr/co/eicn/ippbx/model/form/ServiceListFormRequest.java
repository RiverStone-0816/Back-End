package kr.co.eicn.ippbx.model.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceListFormRequest extends BaseForm {
	@NotNull("대표번호명")
	private String  svcName;
	@NotNull("대표번호")
	private String  svcNumber;
	private String  svcCid;
	@JsonIgnore
	private String companyId;
	private String groupCode;
	private String groupTreeName;
	private Integer groupLevel;
	private Integer serviceLevel = 10;
}
