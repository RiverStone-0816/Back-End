package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class RandomCidResponse {
	private Integer seq;
	private String  number;     // 발신번호
	private String  groupCode;
	private Byte    shortNum; // 단축번호

	private List<OrganizationSummaryResponse> companyTrees;
}
