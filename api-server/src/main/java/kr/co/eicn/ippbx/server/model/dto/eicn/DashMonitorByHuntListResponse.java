package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DashMonitorByHuntListResponse {
    private String name;
    private String queueName; //헌트명
    private Integer customWaitCnt;  //고객 대기수
    private Integer counselWaitCnt;  //상담 대기수
}
