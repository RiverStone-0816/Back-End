package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class DashInboundChartDataResponse {
    private Integer statHour;       // 시간 0 - 23
    private Integer totalCnt;       // 전체콜
    private Integer onlyReadCnt;    // 단순조회
    private Integer connReqCnt;     // 연결요청
    private Integer successCnt;     // 응대호
    private Integer cancelCnt;      // 포기호
    private Integer callbackCnt;    // 콜백
}
