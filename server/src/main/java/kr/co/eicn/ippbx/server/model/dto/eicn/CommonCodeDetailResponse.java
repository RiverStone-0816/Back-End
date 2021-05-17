package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class CommonCodeDetailResponse extends CommonCodeSummaryResponse {
    private Integer sequence;
    private String hide;
    private String script;
}
