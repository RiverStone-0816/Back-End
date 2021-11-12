package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class WebchatBotButtonElementFormRequest {
    private Integer buttonId;
    private Integer blockId;
    private Integer order;
    private String buttonName;
    private String action;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;
    private String nextApiUrl;
    private String nextApiMent;
    private String isResultTemplateEnable;
    private String nextApiResultTemplate;
    private String nextApiNoResultMent;
    private String nextApiErrorMent;
}
