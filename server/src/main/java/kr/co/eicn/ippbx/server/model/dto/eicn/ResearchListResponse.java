package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchList;
import lombok.Data;

import java.util.List;

@Data
public class ResearchListResponse extends ResearchList {
	private List<OrganizationSummaryResponse> companyTrees;
}
