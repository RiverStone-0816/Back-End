package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class OrganizationSummaryResponse {
	private String  groupCode;
	private String  groupName;
	private String  groupTreeName;
	private Integer groupLevel;
}
