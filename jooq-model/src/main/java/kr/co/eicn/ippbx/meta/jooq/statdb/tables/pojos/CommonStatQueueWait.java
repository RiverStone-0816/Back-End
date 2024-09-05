package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.sql.Date;

@Data
public class CommonStatQueueWait {
    private Integer seq;
    private String  companyId;
    private String  groupCode;
    private String  groupTreeName;
    private Integer groupLevel;
    private String  queueName;
    private Date    statDate;
    private Byte    statHour;
    private Integer totalWait;
    private Integer maxWait;
}
