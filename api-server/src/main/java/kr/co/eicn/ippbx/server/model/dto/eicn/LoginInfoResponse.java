package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LoginInfoResponse {
    private String userId;
    private String secureIp;
    private Timestamp insertDate;
    private Timestamp passwordChangeDate;
    private Integer changePasswordDays;
}
