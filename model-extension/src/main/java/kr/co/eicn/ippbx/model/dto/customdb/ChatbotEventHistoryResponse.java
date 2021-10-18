package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatbotEventHistoryResponse {
    private Timestamp sendTime;
    private String chatbotName;
    private String eventName;
    private String profileName;
    private String eventData;
    private String blockName;
    private String isReceive;
}
