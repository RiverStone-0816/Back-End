package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class WebchatBotTreeFormRequest {
    private Integer chatBotId;
    private Integer blockId;
    private Integer rootId;
    private Integer parentId;
    private Integer parentButtonId;
    private Integer level;
    private String treeName;
}
