package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatInbound implements Serializable {
    private Integer seq;
    private String  companyId;
    private String  groupCode;
    private String  groupTreeName;
    private Integer groupLevel;
    private String  serviceNumber;
    private String  huntNumber;
    private String  worktimeYn;
    private String  category;
    private String  ivrTreeName;
    private String  dcontext;
    private Date    statDate;
    private Byte    statHour;
    private Integer total;
    private Integer onlyread;
    private Integer connreq;
    private Integer success;
    private Integer callback;
    private Integer callbackSuccess;
    private Integer transfer;
    private Integer cancel;
    private Integer cancelTimeout;
    private Integer cancelNoanswer;
    private Integer cancelCustom;
    private Integer serviceLevelOk;
    private Integer billsecSum;
    private Integer waitSum;
    private Integer billsecMax;
    private Integer waitMax;
    private Integer waitCancelSum;
    private Integer waitSucc_0_10;
    private Integer waitSucc_10_20;
    private Integer waitSucc_20_30;
    private Integer waitSucc_30_40;
    private Integer waitSucc_40;
    private Integer waitCancel_0_10;
    private Integer waitCancel_10_20;
    private Integer waitCancel_20_30;
    private Integer waitCancel_30_40;
    private Integer waitCancel_40;
}
