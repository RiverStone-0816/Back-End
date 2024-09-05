package kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonTranscribeData implements Serializable {
    private Integer seq;
    private String  companyId;
    private Integer groupcode;
    private String  filepath;
    private String  filename;
    private String  userid;
    private String  hypinfo;
    private String  refinfo;
    private String  status;
    private String  sttstatus;
    private String  recstatus;
    private String  learn;
    private Double  nrate;
    private Double  drate;
    private Double  srate;
    private Double  irate;
    private Double  arate;
}
