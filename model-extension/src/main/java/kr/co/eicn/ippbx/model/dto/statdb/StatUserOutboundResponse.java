package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class StatUserOutboundResponse {
    private Integer outTotal            = 0;    //총 시도콜
    private Integer outSuccess          = 0;    //성공호
    private Integer fails               = 0;    //비수신 = 총 시도콜 - 성공호
    private Integer outBillSecSum       = 0;    //총 통화시간
    private Integer avgBillSec          = 0;    //평균 통화시간 = 총통화시간 / 성공호
    private Float   avgRate             = 0f;   //통화성공률 = 성공호 / 총시도콜
    private Integer callbackCallCount   = 0;    //콜백
    private Integer callbackCallSuccess = 0;    //콜백 성공호
    private Float   callbackProcessRate = 0f;   //콜백 성공률 = 콜백 성공호 / 콜백
    private Integer reserveCallCount    = 0;    //예약
    private Integer reserveCallSuccess  = 0;    //예약 성공호
    private Float   reserveProcessRate  = 0f;   //예약 성공률 = 예약 성공호 / 예약
}
