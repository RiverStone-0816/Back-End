package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

import java.util.List;

@Data
public class StatHuntTotalResponse {
    private Integer tInTotal = 0;
    private Integer tInSuccess = 0;
    private Integer tCallBack = 0;
    private Integer tInBillSecSum = 0;
    private Long tAvgBillSec = 0L;
    private Integer tCancel = 0;
    private Float tAvgRateValue =  0f;
    private Integer tServiceLevelOk = 0;

    private List<StatHuntResponse<?>> statHuntResponse;
}
