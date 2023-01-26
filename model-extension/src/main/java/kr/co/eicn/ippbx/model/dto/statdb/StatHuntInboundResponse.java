package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Data;

@Data
public class StatHuntInboundResponse {
    private String queueName;
    private Integer inTotal = 0;
    private Integer inSuccess = 0;
    private Integer callbackCount = 0;
    private Integer inBillSecSum = 0;
    private Long avgBillSec = 0L;
    private Integer cancel = 0;
    private Float avgRateValue = 0f;
    private Integer serviceLevelOk = 0;

    @JsonIgnore
    public Double getSvcLevelAvg() {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(getServiceLevelOk(), getInSuccess())));
    }
}
