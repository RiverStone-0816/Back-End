package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CidInfoSearchRequest extends PageForm {
    @PageQueryable
    private String extension;
    @PageQueryable
    private String localPrefix;
    @PageQueryable
    private String cid;
    @PageQueryable
    private String billingNumber;
}
