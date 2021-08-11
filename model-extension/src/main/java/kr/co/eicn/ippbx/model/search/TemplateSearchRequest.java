package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TemplateSearchRequest extends PageForm {
    @PageQueryable
    private String type;
    @PageQueryable
    private String metaType;
    @PageQueryable
    private String userName;
    @PageQueryable
    private String mentName;
}
