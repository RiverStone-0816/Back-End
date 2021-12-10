package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class WebchatServiceSummaryInfoResponse {
    private Integer seq;
    private String channelName;
    private String senderKey;
    private Boolean enableChat;
    private String displayCompanyName;
}
