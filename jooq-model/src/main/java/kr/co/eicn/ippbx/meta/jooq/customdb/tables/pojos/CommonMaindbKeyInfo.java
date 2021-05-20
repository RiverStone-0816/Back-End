package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonMaindbKeyInfo implements Serializable {
    private String  keyValue;
    private String  customId;
    private Integer groupId;
}
