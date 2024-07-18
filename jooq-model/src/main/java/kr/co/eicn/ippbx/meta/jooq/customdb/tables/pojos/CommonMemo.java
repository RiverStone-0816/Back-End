package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class CommonMemo implements Serializable {
    private Long      id;
    private String    companyId;
    private String    content;
    private String    user;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;
    private String    title;
    private Boolean   bookmarked;
}
