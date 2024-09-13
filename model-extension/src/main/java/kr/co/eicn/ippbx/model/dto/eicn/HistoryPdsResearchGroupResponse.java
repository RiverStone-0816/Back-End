package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class HistoryPdsResearchGroupResponse {
    private Integer pdsGroupId;
    private String  executeId;
    private String  executeName;
    private Integer pdsType;
    private String  connectData;
}
