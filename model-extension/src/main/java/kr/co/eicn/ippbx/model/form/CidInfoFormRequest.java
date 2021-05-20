package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class CidInfoFormRequest {
	private String peer;
	private String localPrefix;
	private String cid;
	private String billingNumber;
	private Byte dialStatus;
	private Byte logoutStatus;
	private Byte firstStatus;
}
