package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class StatOutboundResponse {
    private Integer total   = 0; //총시도콜
    private Integer success = 0; //성공호
    private Integer cancel  = 0; //비수신 = 총시도콜 - 성공호

    private Double  successAvg = 0d; //통화성공률 = 성공호 / 총시도콜
    private Integer billSecSum = 0;  //총 통화시간
    private Long    billSecAvg = 0L; //평균 통화시간 = 총통화시간 / 성공호
}
