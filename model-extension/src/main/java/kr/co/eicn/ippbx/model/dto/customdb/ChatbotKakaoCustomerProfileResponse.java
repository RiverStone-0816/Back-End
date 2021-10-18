package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatbotKakaoCustomerProfileResponse {
    private Integer seq;
    private String chatbotName;
    private Timestamp authenticationDate;
    private String profileName;
    private String phoneNumber;
    private String requestUserPlusFriendUserKey;
    private String maindbCustomName;
}
