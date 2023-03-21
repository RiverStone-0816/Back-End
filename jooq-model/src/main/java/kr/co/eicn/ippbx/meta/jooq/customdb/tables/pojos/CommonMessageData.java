package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonMessageData implements Serializable {
    private Integer   seq;
    private String    sendCliKey;
    private String    title;
    private String    status;
    private String    resultStatus;
    private Timestamp insertTime;
    private Timestamp sendTime;
    private Timestamp resultTime;
    private String    service;
    private String    callback;
    private String    phoneNumber;
    private String    message;
    private String    userid;
    private String    projectId;
    private String    apiKey;
    private String    attachFile;
    private String    companyId;
    private String    resDataMsgkey;
    private String    resCode;
    private String    resMessage;
    private String    resDataCode;
    private String    resDataMessage;
    private Integer   retryCnt;
}
