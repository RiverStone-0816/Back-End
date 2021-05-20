package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCodeDetailResponse extends CommonCodeSummaryResponse {
    private Integer sequence;
    private String hide;
    private String script;
}
