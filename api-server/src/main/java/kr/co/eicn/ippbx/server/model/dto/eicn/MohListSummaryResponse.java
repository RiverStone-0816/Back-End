package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class MohListSummaryResponse {
	private String  category;
	private String  mohName;
	private String  companyId;
	private Integer companySeq;
	private String  directory;
	private String  fileName;
}
