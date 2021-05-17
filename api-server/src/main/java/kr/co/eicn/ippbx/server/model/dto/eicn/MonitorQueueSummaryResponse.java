package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class MonitorQueueSummaryResponse {
    private SummaryQueueResponse queue;
    private Integer customWaitCnt = 0;  //고객대기
    private Integer totalUser = 0;
    private Integer loginUser = 0;
    private Integer logoutUser = 0;

    private Map<Integer, Integer> statusToUserCount = new LinkedHashMap<>(); //상담원 상태 리스트
}
