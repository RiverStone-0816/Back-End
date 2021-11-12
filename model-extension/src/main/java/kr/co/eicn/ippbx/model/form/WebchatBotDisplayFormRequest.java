package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class WebchatBotDisplayFormRequest {
    private Integer blockId;
    private Integer order;
    private String type;
}
