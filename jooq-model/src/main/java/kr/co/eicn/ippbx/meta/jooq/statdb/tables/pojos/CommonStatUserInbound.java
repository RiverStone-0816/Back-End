package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatUserInbound implements Serializable {
    private Integer seq;
    private String  companyId;
    private String  groupCode;
    private String  groupTreeName;
    private Integer groupLevel;
    private String  userid;
    private String  serviceNumber;
    private String  huntNumber;
    private String  worktimeYn;
    private String  category;
    private String  dcontext;
    private Date    statDate;
    private Byte    statHour;
    private Integer inTotal;
    private Integer inSuccess;
    private Integer inHuntNoanswer;
    private Integer inBillsecSum;
    private Integer inWaitsecSum;
    private Integer transferer;
    private Integer transferee;
    private Integer transfereeSucc;
    private Integer callbackDist;
    private Integer callbackSucc;
    private Integer serviceLevelOk;
}
