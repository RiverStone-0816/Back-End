package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

@Data
public class ChatbotSendEventDataResponse {
    private String botId;
    private String botName;
    private String requestUserId;
    private String requestUserPlusFriendUserKey;
    private String nickName;
    private String appUserId;
}
