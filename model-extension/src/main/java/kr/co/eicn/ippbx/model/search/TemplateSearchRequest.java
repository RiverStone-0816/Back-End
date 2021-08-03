package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import kr.co.eicn.ippbx.util.page.PageForm;
import lombok.Data;

@Data
public class TemplateSearchRequest extends PageForm {
    private TalkTemplate type;
    private String metaType;
    private String userName;
    private String mentName;
}
