package kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonTalkRoom implements Serializable {
    private Integer   seq;
    private String    roomId;
    private Timestamp roomStartTime;
    private Timestamp roomLastTime;
    private String    roomStatus;
    private Integer   maindbGroupId;
    private String    maindbCustomId;
    private String    maindbCustomName;
    private String    userid;
    private String    userKey;
    private String    senderKey;
    private String    companyId;
    private String    roomName;
    private String    scheduleKind;
    private String    scheduleData;
}
