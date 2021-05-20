package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class OrganizationSummaryResponse {
	private String  groupCode;
	private String  groupName;
	private String  groupTreeName;
	private Integer groupLevel;
}
