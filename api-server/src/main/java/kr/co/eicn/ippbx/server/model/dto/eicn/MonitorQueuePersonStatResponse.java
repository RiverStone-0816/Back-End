package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class MonitorQueuePersonStatResponse {
    private PersonListSummary person;
    private String  isLogin;
    private String  isPhone;  //전화기상태(Y,N)
    private String  queueName;    //인입헌트
    private String  queueHanName;    //인입헌트
    private String  inOut;    //수/발신(I,O)
    private String  customNumber;    //고객번호
    private Integer inboundSuccess = 0;    //수신건수
    private Integer outboundSuccess = 0;   //발신건수
    private Integer statTotal = 0;  //합계
    private Integer billSecSum = 0; //총통화시간
    private Long billSecAvg = 0L; //평균 통화시간
    
    public Integer getBillSecTotalSum(Integer inbound, Integer outbound) {
        return (inbound == null ? 0 : inbound) + (outbound == null ? 0 : outbound);
    }

    public Long getBillSecAvg(Integer billsecSum, Integer success) {
        if (billsecSum == null || success == null)
            return 0L;

        return (billsecSum > 0 ? (long) (billsecSum / success) : 0);
    }

    public void setTotalStat() {
        statTotal = inboundSuccess + outboundSuccess;
    }

    public void setBillSecondAverage() {
        if (statTotal > 0 && billSecSum > 0)
            billSecAvg = (long) (billSecSum / statTotal);
    }
}
