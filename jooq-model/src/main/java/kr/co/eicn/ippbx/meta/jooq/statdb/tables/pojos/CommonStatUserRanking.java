package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.sql.Date;

@Data
public class CommonStatUserRanking {
    private Integer seq;
    private String  companyId;
    private String  groupCode;
    private String  groupTreeName;
    private Integer groupLevel;
    private String  userid;
    private String  worktimeYn;
    private Date    statDate;
    private Integer totalSuccess;
    private Integer inSuccess;
    private Integer outSuccess;
    private Integer totalBillsecSum;
    private Integer inBillsecSum;
    private Integer outBillsecSum;
    private Integer callbackSuccess;
}
