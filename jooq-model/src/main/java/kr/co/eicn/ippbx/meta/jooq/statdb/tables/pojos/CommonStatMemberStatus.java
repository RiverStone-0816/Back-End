package kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class CommonStatMemberStatus implements Serializable {
    private Integer seq;
    private Date statDate;
    private Byte statHour;
    private Integer status;
    private Integer total;
    private Long diffSum;
    private String userid;
    private String companyId;
}
