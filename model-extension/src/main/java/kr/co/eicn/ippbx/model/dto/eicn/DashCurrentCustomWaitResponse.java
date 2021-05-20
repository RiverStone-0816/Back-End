package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.TreeMap;

@Data
public class DashCurrentCustomWaitResponse {
    private String title;
    private Integer currentWaitCnt;  //현재대기호 수
    private Integer waitSecTotal; //총대기시간
    private Integer waitSecAve; //평균대기시간
    private Integer waitSecMax; ///최대 대기시간
    private Integer connReqCnt; // 연결요청 건수
    private Integer cancelCnt; // 연결취소 건수
    private Float customCancelRateValue; //포객포기율

    private TreeMap<Integer, Float> hourToAvgWaitingTime = new TreeMap<>();
}
