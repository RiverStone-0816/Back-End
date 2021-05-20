package kr.co.eicn.ippbx.front.model.search;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;

@Data
public class OrganizationPanCondition {
    @PageQueryable
    private String keyword;
}
