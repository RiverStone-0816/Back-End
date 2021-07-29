package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import lombok.Data;

@Data
public class TemplateSearchRequest {
    private TalkTemplate type;
    private String metaType;
    private String userName;
    private String mentName;
}
