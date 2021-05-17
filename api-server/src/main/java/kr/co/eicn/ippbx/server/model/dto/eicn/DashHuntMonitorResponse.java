package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;
import java.util.TreeMap;

@Data
public class DashHuntMonitorResponse {
    private String queueName;
    private String queueHanName;
    private Integer customWaitCnt;  //고객대기 수
    private Integer counselWaitCnt; //상담대기 수
    private Integer counselWaitNoLoginCnt; //비로그인 대기 수
    private Integer callingCnt; ///통화중 수
    private Integer etcCnt; //후처리 등 기타
    private float rateValue; //응답률
    private Integer loginCnt; //로그인 상담원

    private TreeMap<Integer, DashResultChartResponse> hourToCurrentCnt = new TreeMap<>();

    private List<DashQueueMemberResponse> queueMemberList;
}