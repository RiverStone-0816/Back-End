package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class StatOutboundResponse {
    private Integer total = 0;  //전체콜
    private Integer success = 0;    //응대호
    private Integer cancel = 0; //비수신

    private Double successAvg = 0d;     //통화성공률
    private Integer billSecSum = 0; //총통화시간
    private Long billSecAvg = 0L; //평균 통화시간
}