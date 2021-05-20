package kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonExecutePDSCustomInfo implements Serializable {
    private Integer   seq;
    private String    customId;
    private String    executeId;
    private Integer   pdsGroupId;
    private String    numberField;
    private Byte      fieldSeq;
    private String    customNumber;
    private String    rid;
    private String    status;
    private String    ttsData;
    private String    clickKey;
    private String    hangupCause;
    private String    hangupMsg;
    private Timestamp hangupTime;
    private String    readStatus;
    private String    companyId;
    private String    userData1;
    private String    userData2;
    private String    userData3;
}
