package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class WebchatServiceInfoResponse {
    private Integer seq;
    private String channelName;
    private String senderKey;
    private String enableChat;
    private String displayCompanyName;
    private String message;
    private String imageFileName;
    private String backgroundColor;
    private String profileFileName;
}
