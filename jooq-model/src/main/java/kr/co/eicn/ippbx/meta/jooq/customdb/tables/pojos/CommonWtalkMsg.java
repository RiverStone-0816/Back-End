package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonWtalkMsg implements Serializable {
    private Integer   seq;
    private String    roomId;
    private String    channelType;
    private Timestamp insertTime;
    private String    sendReceive;
    private String    companyId;
    private String    userid;
    private String    userKey;
    private String    senderKey;
    private String    messageId;
    private String    time;
    private String    type;
    private String    content;
    private String    attachment;
    private String    extra;
    private String    deleteRequest;
}
