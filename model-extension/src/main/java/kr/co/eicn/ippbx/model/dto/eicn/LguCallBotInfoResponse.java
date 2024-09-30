package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class LguCallBotInfoResponse {
    private String tenantId;
    private String callbotKey;
    private String convApiUrl;
}
