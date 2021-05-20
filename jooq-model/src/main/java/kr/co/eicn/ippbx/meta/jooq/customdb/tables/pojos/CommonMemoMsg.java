package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonMemoMsg implements Serializable {
    private Integer   seq;
    private String    messageId;
    private String    sendUserid;
    private String    receiveUserid;
    private Timestamp insertTime;
    private String    sendReceive;
    private String    readYn;
    private String    content;
}
