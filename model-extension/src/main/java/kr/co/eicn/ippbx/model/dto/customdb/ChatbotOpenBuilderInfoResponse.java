package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

@Data
public class ChatbotOpenBuilderInfoResponse {
    private Integer seq;
    private String botName;
    private String blockName;
    private String blockId;
    private String responseType;
    private String responseGetUrl;
    private String responseParamNames;
    private String eventName;
    private String useYn;
}
