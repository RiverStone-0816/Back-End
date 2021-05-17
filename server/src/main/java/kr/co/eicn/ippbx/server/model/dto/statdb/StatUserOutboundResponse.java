package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

import java.sql.Date;

@Data
public class StatUserOutboundResponse {
    private Integer outTotal = 0;
    private Integer outSuccess = 0;
    private Integer fails = 0;
    private Integer outBillSecSum = 0;
    private Integer avgBillSec = 0;
    private Float avgRate = 0f;
    private Integer callbackCallCount = 0;
    private Integer callbackCallSuccess = 0;
    private Float callbackProcessRate = 0f;
    private Integer reserveCallCount = 0;
    private Integer reserveCallSuccess = 0;
    private Float reserveProcessRate = 0f;
}
