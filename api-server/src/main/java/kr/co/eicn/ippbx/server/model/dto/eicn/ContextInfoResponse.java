package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class ContextInfoResponse {
    private String context;
    private String name;
    private String isWebVoice;
}
