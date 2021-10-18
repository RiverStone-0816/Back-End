package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

@Data
public class ChatKaKaoProfileInfoResponse {
    private Integer seq;
    private String nickName;
    private String profileImageUrl;
    private String phoneNumber;
    private String requestUserPlusFriendUserKey;
    private String requestUserId;
}
