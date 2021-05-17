package kr.co.eicn.ippbx.server.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class MonitorQueueStatResponse {
    private String  queueName;
    private String  queueHanName;
    private Integer connReq = 0;    //연결요청
    private Integer success = 0;    //응대호
    private Integer callback = 0;   //콜백
    private Double responseRate = 0d; //응대율

    private Integer serviceCounselorCnt = 0;   //근무상담사
    private Integer customWaitCnt = 0;  //고객대기 수
    private Integer counselorWaitCnt = 0;  //상담원대기 수
    private Integer inboundCallCnt = 0;     //상담원I/B통화 수
    private Integer outboundCallCnt = 0;    //상담원I/B통화 수
    private Integer postprocessStatusCnt = 0;  //후처리 수
    private Integer etcCnt = 0;      //기타 수

    @JsonIgnore
    public Integer getWorkingPersonCount() {
        return counselorWaitCnt + inboundCallCnt + outboundCallCnt + postprocessStatusCnt + etcCnt;
    }
}
