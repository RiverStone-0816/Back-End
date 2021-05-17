package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem;
import lombok.Data;

import java.util.List;

@Data
public class ResearchItemSummaryResponse extends ResearchItem {
	private List<String> answers; // 답변 목록
	private List<OrganizationSummaryResponse> companyTrees;
}
