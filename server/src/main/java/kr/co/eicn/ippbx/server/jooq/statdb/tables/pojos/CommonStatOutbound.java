package kr.co.eicn.ippbx.server.jooq.statdb.tables.pojos;

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
    private String  fromOrg;
    private Date statDate;
    private Byte    statHour;
    private Integer total;
    private Integer success;
    private Integer billsecSum;
    private Integer waitSum;
    private String  dcontext;
    private String  worktimeYn;
}
