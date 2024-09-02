package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatOutbound implements Serializable {
    private Integer seq;
    private String  companyId;
    private String  groupCode;
    private String  groupTreeName;
    private Integer groupLevel;
    private String  cidNumber;
    private String  fromOrg;
    private String  worktimeYn;
    private String  dcontext;
    private Date    statDate;
    private Byte    statHour;
    private Integer total;
    private Integer success;
    private Integer billsecSum;
    private Integer waitSum;
}
