package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

@Data
public class ChatbotEventResponse {
    private String chatbotId;
    private String chatbotName;
    private String eventName;
    private String blockName;
}
