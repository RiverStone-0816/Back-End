package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.sql.Date;

@Data
public class ChatbotHistoryResponse {
    private int seq;
    private String botName;
    private Date insertDate;
    private String nickName;
    private String requestUserPlusFriendUserKey;
    private String requestBlockName;
    private String requestUtterance;
}
