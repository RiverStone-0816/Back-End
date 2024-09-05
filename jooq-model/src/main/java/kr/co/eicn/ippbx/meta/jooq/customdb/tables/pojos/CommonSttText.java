package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonSttText implements Serializable {
    private Integer   seq;
    private String    messageId;
    private Timestamp insertTime;
    private String    callUniqueid;
    private String    ipccUserid;
    private String    myExtension;
    private String    kind;
    private String    text;
    private Integer   startMs;
    private Integer   stopMs;
    private String    kmsKeyword;
    private String    remindYn;
}
