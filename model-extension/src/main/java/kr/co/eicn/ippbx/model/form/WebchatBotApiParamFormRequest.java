package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.ApiParameterType;
import lombok.Data;

@Data
public class WebchatBotApiParamFormRequest {
    private Integer buttonId;
    private ApiParameterType type;
    private String paramName;
    private String displayName;
}
