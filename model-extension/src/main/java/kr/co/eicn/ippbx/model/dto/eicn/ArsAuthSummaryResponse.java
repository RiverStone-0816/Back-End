package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ArsAuthSummaryResponse {
    private Integer   seq;
    private Timestamp insertDate;
    private String    userid;
    private String    number;
    private String    sessionId;
    private String    authNum;
    private String    arsStatus;
}
