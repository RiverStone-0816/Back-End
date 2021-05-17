package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class WebVoiceResponse {
    private String context;
    private Integer ivrCode;
    private String headerUse;
    private String textareaUse;
    private String inputareaUse;
    private String controlUse;

    private List<WebVoiceItemsResponse> items;
}
