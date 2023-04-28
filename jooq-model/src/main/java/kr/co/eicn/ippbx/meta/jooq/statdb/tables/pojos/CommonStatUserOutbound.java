package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatUserOutbound implements Serializable {
    private Integer seq;
    private String companyId;
    private String groupCode;
    private String groupTreeName;
    private Integer groupLevel;
    private String userid;
    private String userStatYn;
    private String fromOrg;
    private String worktimeYn;
    private String dcontext;
    private Date statDate;
    private Byte statHour;
    private Integer outTotal;
    private Integer outSuccess;
    private Integer outBillsecSum;
    private Integer callbackCallCnt;
    private Integer callbackCallSucc;
    private Integer reserveCallCnt;
    private Integer reserveCallSucc;
}
