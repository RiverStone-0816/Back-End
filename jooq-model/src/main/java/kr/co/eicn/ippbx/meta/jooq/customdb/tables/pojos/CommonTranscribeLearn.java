package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonTranscribeLearn implements Serializable {
    private Integer seq;
    private String  companyId;
    private String  groupname;
    private String  learngroupcode;
    private String  learnstatus;
    private String  learnfilename;
}