package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PersonLastStatusInfoResponse {
    private Timestamp endDate;
    private String phoneid;
    private String phonename;
    private Integer nextStatus;
    private String inOut;
}
