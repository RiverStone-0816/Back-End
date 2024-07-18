package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonLearnGroup implements Serializable {
    private Integer seq;
    private String companyId;
    private String groupName;
    private String learnGroupCode;
    private String learnStatus;
    private String learnFileName;
}
