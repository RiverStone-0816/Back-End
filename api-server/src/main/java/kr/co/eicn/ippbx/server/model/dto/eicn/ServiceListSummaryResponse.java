package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class ServiceListSummaryResponse {
	private Integer seq;
	private String  svcName;
	private String  svcNumber;
	private String  svcCid;
	private String  companyId;
	private String groupCode;
	private String groupTreeName;
	private Integer groupLevel;
	private Integer serviceLevel;
	private String hostName;

	private List<OrganizationSummaryResponse> organizationSummary;
}
