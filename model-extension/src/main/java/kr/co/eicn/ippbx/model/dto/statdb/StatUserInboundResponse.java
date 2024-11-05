package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class StatUserInboundResponse {
    private Integer total             = 0;  //요청호
    private Integer success           = 0;  //응대호
    private Integer billSecSum        = 0;  //총 통화시간
    private Integer avgBillSec        = 0;  //평균 통화시간 = 총통화시간 / 응대호
    private Integer avgWaitSec        = 0;  //평균 대기시간 = (총대기시간 - 총포기호대기시간) / 응대호
    private Integer cancel            = 0;  //개인비수신
    private Float   avgRate           = 0f; //응대율 = 응대호 / 요청호
    private Integer transferCount     = 0;  //전환건수
    private Float   transferCountRate = 0f; //전환율 = 전환건수 / 요청호
}
