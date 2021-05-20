package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchListResponse extends ResearchList {
    private List<OrganizationSummaryResponse> companyTrees;
}
