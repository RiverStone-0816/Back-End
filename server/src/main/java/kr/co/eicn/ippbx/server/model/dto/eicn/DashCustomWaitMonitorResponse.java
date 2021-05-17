package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DashCustomWaitMonitorResponse {
    private String title;
    private Integer customWaitCnt;  //고객대기수
    private Integer counselWaitCnt; //상담대기
    private Integer callingCnt; ///통화중수
    private Integer etcCnt; //후처리 등 기타
}
