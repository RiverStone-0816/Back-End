package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class ChatbotOpenBuilderBlockFormRequest {
    private String chatbotId;
    private String chatbotName;
    private String blockName;
    private String blockId;
    private String responseType;
    private String responseGetUrl;
    private String responseParamNames;
    private String eventName;
    private String useYn;
}
