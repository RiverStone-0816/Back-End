package kr.co.eicn.ippbx.model;

import lombok.Data;

@Data
public class OrganizationPerson {
	private String id;
	private String idName;
	private String groupCode;
	private Integer groupLevel;
	private String  extension;
	private String  peer;
	private String  companyId;
	private String  hpNumber;
	private String  emailInfo;
}
