package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.BlockType;
import lombok.Data;

@Data
public class WebchatBotBlockFormRequest {
    private String name;
    private String keyword;
    private Boolean isTemplateEnable;
    private Integer posX;
    private Integer posY;
    private BlockType type;
    private Integer formBlockId;
}
