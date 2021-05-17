package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class WebVoiceItemsResponse {
    private String areaType;
    private String itemType;
    private String itemName;
    private String itemValue;
    private String isView;
    private Integer sequence;
    private Integer inputMaxLen;
}
