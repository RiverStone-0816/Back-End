package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class ScoreMonitorResponse {
    private String userId;
    private Integer inCnt = 0;
    private Integer outCnt = 0;
    private Integer TotalCnt = 0;
    private Integer talkCnt = 0;

}
