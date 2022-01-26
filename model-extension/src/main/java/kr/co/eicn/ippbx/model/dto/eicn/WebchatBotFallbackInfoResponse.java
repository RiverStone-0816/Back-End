package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.FallbackAction;
import lombok.Data;

@Data
public class WebchatBotFallbackInfoResponse {
    private String fallbackMent;
    private FallbackAction fallbackAction;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;
    private Boolean enableCustomerInput;
}
