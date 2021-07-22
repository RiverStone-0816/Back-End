package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class ScoreMonitorResponse {
    private String userId; //유저아이디
    private Integer inCnt = 0; //인바운드
    private Integer outCnt = 0; //아웃바운드
    private Integer totalCnt = 0; //인+아웃 합
    private Integer talkCnt = 0; //톡상담

}
