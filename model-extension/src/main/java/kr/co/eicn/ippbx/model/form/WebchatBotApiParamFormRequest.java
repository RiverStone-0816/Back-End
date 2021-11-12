package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class WebchatBotApiParamFormRequest {
    private Integer buttonId;
    private String type;
    private String paramName;
    private String displayName;
}
