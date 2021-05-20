package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class CommonVocCustomList implements Serializable {
    private Integer   seq;
    private Timestamp insertDate;
    private Integer   vocGroupId;
    private String    sender;
    private String    customNumber;
    private String    companyId;
}
