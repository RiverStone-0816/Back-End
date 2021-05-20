package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonChattRoom implements Serializable {
    private String    roomId;
    private String    roomName;
    private String    roomNameChange;
    private String    memberMd5;
    private Timestamp startTime;
    private Timestamp lastTime;
    private String    lastMsg;
    private String    lastMsgType;
    private String    lastMsgSendReceive;
    private String    lastUserid;
    private String    makeUserid;
    private Integer   curMemberCnt;
    private Integer   orgMemberCnt;
}
