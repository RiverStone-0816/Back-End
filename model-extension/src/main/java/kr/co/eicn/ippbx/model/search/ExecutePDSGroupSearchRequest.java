package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExecutePDSGroupSearchRequest extends PageForm {
    @PageQueryable
    private Integer executeId;
    @PageQueryable
    private Timestamp createdStartDate;
    @PageQueryable
    private Timestamp createdEndDate;
}
