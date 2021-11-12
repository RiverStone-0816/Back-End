package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class WebchatBotInfoResponse {
    private Integer id;
    private String name;
    private String fallbackMent;
    private String fallbackAction;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;
}
