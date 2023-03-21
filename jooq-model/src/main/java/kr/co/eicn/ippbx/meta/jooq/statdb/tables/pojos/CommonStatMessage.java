package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatMessage implements Serializable {
    private Date statDate;
    private Byte    statHour;
    private String  service;
    private String  projectId;
    private String  apiKey;
    private String  userid;
    private String  resCode;
    private String  resDataCode;
    private Integer total;
}
