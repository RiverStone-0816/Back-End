package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class HistoryPdsGroupResponse {
    private Integer pdsGroupId;
    private String  executeId;
    private String  executeName;
    private Integer pdsType;
    private Integer resultType;
}
