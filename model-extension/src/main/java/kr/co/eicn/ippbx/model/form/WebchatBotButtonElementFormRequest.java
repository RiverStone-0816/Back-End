package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.enums.IsTemplateEnable;
import lombok.Data;

@Data
public class WebchatBotButtonElementFormRequest {
    private Integer buttonId;
    private Integer blockId;
    private Integer order;
    private String buttonName;
    private ButtonAction action;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;
    private String nextApiUrl;
    private String nextApiMent;
    private IsTemplateEnable isResultTemplateEnable;
    private String nextApiResultTemplate;
    private String nextApiNoResultMent;
    private String nextApiErrorMent;
}
