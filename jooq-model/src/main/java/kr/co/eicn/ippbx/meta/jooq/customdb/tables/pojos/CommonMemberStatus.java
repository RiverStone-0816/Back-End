package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonMemberStatus implements Serializable {
    private Integer   seq;
    private Timestamp startDate;
    private Timestamp endDate;
    private String    phoneid;
    private String    phonename;
    private String    status;
    private String    nextStatus;
    private String    inOut;
    private String    companyId;
}
