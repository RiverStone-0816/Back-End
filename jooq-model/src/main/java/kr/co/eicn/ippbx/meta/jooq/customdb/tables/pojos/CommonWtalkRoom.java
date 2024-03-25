package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonWtalkRoom implements Serializable {
    private Integer   seq;
    private String    roomId;
    private Timestamp roomStartTime;
    private Timestamp roomLastTime;
    private String    roomStatus;
    private String    roomMode;
    private Integer   maindbGroupId;
    private String    maindbCustomId;
    private String    maindbCustomName;
    private String    authPhoneNumber;
    private String    userid;
    private String    userKey;
    private String    channelType;
    private String    senderKey;
    private String    companyId;
    private String    roomName;
    private String    scheduleKind;
    private String    scheduleData;
    private String    scheduleStatYn;
    private String    scheduleWorktimeYn;
    private String    isAutoEnable;
    private String    isCustomUploadEnable;
    private Integer   audioUseCnt;
    private Integer   videoUseCnt;
    private Integer   keyId;
    private String    lastUserYn;
    private String    lastType;
    private String    lastSendReceive;
    private String    lastContent;
}
