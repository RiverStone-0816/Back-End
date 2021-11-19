package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class WebchatBotBlockFormRequest {
    private String name;
    private String keyword;
    private Boolean isTemplateEnable;
    private Integer posX;
    private Integer posY;
}
