package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Data;

@Data
public class StatHuntInboundResponse {
    private String  queueName;              //큐그룹명
    private Integer inTotal        = 0;     //I/B 연결요청
    private Integer inSuccess      = 0;     //응대호
    private Integer cancel         = 0;     //포기호
    private Integer callbackCount  = 0;     //콜백
    private Integer inBillSecSum   = 0;     //I/B 총 통화시간
    private Long    avgBillSec     = 0L;    //I/B 평균 통화시간 = I/B 총 통화시간 / 응대호
    private Float   avgRateValue   = 0f;    //호응답률 = 응대호 / I/B 연결요청
    private Integer serviceLevelOk = 0;     //서비스레벨 응대호

    //서비스레벨 호응답률 = 서비스레벨 응대호 / 응대호
    @JsonIgnore
    public Double getSvcLevelAvg() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getServiceLevelOk(), getInSuccess())));
    }
}
