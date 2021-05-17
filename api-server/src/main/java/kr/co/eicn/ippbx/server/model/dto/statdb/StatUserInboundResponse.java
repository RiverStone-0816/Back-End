package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class StatUserInboundResponse {
    private Integer total = 0;
    private Integer success = 0;
    private Integer billSecSum = 0;
    private Integer avgBillSec = 0;
    private Integer avgWaitSec = 0;
    private Integer cancel = 0;
    private Float avgRate = 0f; //응대율
    private Integer transferCount = 0;  //전환건수
    private Float transferCountRate = 0f;   //전환율
}