package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonEicnCdr implements Serializable {

    private Integer   seq;
    private Timestamp ringDate;
    private Timestamp waitingDate;
    private Timestamp dialupDate;
    private Timestamp hangupDate;
    private Integer   duration;
    private Integer   billsec;
    private Integer   waitsec;
    private String    inOut;
    private String    dcontext;
    private String    peer;
    private String    src;
    private String    dst;
    private String    callstatus;
    private String    detailCallstatus;
    private String    recordInfo;
    private String    recordFile;
    private String    uniqueid;
    private String    dstUniqueid;
    private String    channel;
    private String    dstChannel;
    private String    iniNum;
    private String    secondNum;
    private String    worktimeYn;
    private String    statYn;
    private String    ivrKey;
    private String    queueStrategy;
    private String    queueSequence;
    private String    calleeHangup;
    private String    webVoiceInfo;
    private String    vipBlack;
    private String    userid;
    private String    groupCode;
    private String    groupTreeName;
    private Integer   groupLevel;
    private String    companyId;
    private String    host;
    private String    hangupCause;
    private String    cmpClickKey;
    private String    cmpClickFrom;
    private Integer   cmpGroupId;
    private String    cmpCustomid;
    private Integer   turnOverCnt;
    private String    turnOverKind;
    private String    turnOverNumber;
    private String    turnOverUniqueid;
    private String    etc1;
    private String    etc2;
    private String    etc3;
    private String    etc4;
    private String    etc5;
}
