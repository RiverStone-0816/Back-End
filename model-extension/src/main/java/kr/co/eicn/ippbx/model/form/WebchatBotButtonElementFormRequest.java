package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.ButtonAction;
import lombok.Data;

@Data
public class WebchatBotButtonElementFormRequest {
    private Integer blockId;
    private Integer order;
    private String buttonName;
    private ButtonAction action;
    private String actionData;
    private String nextApiMent;
    private Boolean isResultTemplateEnable;
    private String nextApiResultTemplate;
    private String nextApiNoResultMent;
    private String nextApiErrorMent;
}
