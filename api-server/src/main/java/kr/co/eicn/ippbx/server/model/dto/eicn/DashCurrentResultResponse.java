package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
public class DashCurrentResultResponse {
    private String title;
    private Integer currentCallingCnt;  //실시간 통화중
    private Integer waitInCnt; //수신대기
    private Integer etcStatusCnt; ///후처리 등 기타 상태
    private Integer loginCnt; //로그인 상담원

    private Map<Integer, DashResultChartResponse> hourToCurrentCnt = new HashMap<>();
}
