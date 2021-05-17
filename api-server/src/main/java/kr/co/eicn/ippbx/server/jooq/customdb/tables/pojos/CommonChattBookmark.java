package kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonChattBookmark implements Serializable {
    private Integer seq;
    private String  userid;
    private String  memberid;
}
