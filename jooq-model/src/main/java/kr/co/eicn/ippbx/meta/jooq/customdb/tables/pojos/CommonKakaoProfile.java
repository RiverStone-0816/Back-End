package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonKakaoProfile implements Serializable {
    private Integer   seq;
    private Timestamp insertDate;
    private Timestamp updateDate;
    private String    botId;
    private String    requestUserId;
    private String    requestUserPlusfriendUserkey;
    private String    nickname;
    private String    profileImageUrl;
    private String    phoneNumber;
    private String    email;
    private String    appUserId;
    private Integer   maindbGroupId;
    private String    maindbCustomId;
    private String    maindbCustomName;
}
