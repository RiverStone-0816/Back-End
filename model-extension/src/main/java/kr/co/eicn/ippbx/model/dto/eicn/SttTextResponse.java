package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SttTextResponse {
    private Integer seq;
    private Timestamp insertTime;
    private String callUniqueid;
    private String ipccUserid;
    private String myExtension;
    private String kind;
    private String text;
    private Integer startMs;
    private Integer stopMs;
    private String kmsKeyword;
}
