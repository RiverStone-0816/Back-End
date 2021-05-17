package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class CompanyTreeLevelNameResponse {
	private Integer groupLevel;
	private String  groupTreeName;
}
