package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.DisplayType;
import lombok.Data;

@Data
public class WebchatBotDisplayFormRequest {
    private Integer blockId;
    private Integer order;
    private DisplayType type;
}
