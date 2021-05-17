package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DashTotalMonitorResponse {
    private String title;
    private Integer customWaitCnt;  //고객대기 수
    private Integer counselWaitCnt; //상담대기 수
    private Integer callingCnt; ///통화중 수
    private Integer etcCnt; //후처리 등 기타
    private float rateValue; //응답률
}
