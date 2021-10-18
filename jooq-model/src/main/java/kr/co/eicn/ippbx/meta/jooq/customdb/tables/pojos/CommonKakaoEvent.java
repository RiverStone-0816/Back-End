package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonKakaoEvent implements Serializable {
    private Integer   seq;
    private Timestamp insertDate;
    private Timestamp updateDate;
    private String    botId;
    private String    botName;
    private String    eventName;
    private String    userType;
    private String    userId;
    private String    userName;
    private String    userData;
    private String    taskId;
    private String    responseBlockId;
    private String    responseBlockName;
}
