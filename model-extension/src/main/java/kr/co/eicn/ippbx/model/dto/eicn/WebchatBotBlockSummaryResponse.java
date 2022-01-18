package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class WebchatBotBlockSummaryResponse {
    private Integer botId;
    private String botName;
    private Integer blockId;
    private String blockName;
}
