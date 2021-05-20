package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchItemSummaryResponse extends ResearchItem {
    private List<String> answers; // 답변 목록
    private List<OrganizationSummaryResponse> companyTrees;
}
