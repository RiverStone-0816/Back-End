package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonTranscribeGroup implements Serializable {
    private Integer seq;
    private String companyId;
    private String groupName;
    private String userId;
    private String status;
    private Integer fileCnt;
    private Double recRate;

}
