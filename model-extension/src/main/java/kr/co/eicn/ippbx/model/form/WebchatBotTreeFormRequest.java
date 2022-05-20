package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.BlockType;
import lombok.Data;

@Data
public class WebchatBotTreeFormRequest {
    private Integer chatBotId;
    private Integer blockId;
    private Integer rootId;
    private BlockType type;
    private Integer parentId;
    private Integer parentButtonId;
    private Integer level;
    private String treeName;
}
