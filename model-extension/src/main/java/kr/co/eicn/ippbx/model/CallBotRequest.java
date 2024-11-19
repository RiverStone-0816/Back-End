package kr.co.eicn.ippbx.model;

import lombok.Data;

@Data
public class CallBotRequest {
    private String callUuid;
    private String createdDt;
    private String tenantId;
    private String custom_ani;

    public CallBotRequest(String callUuid, String createdDt, String tenantId, String custom_ani) {
        this.callUuid = callUuid;
        this.createdDt = createdDt;
        this.tenantId = tenantId;
        this.custom_ani = custom_ani;
    }
}
