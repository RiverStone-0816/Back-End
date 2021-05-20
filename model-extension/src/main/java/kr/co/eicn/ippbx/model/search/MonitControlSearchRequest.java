package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import kr.co.eicn.ippbx.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonitControlSearchRequest extends PageQueryableForm {
    @PageQueryable
    private String groupCode;
}
