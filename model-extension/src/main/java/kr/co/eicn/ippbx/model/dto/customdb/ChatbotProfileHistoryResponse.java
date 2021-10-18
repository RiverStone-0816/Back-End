package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatbotProfileHistoryResponse {
    private String nickName;
    private String profileImageUrl;
    private String phoneNumber;
    private String requestUserPlusFriendUserKey;
    private String requestUserId;
    private List<ChatbotHistoryResponse> messages = new ArrayList<>();
}
