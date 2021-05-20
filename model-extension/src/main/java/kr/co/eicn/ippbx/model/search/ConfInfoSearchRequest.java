package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfInfoSearchRequest extends PageForm {
    @PageQueryable
    private Integer reserveYear;
    @PageQueryable
    private Integer reserveMonth;
    @PageQueryable
    private String roomNumber;
}
