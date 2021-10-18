package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class ChatbotSendEventFormRequest {
    private String botId;
    private String botName;
    private String userType;
    private String userId;
    private String userName;
    private String eventName;
    private String eventData;
}
