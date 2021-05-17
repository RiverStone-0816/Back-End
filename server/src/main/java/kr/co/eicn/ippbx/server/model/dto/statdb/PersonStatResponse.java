package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class PersonStatResponse {
    private Integer receive;        //수신
    private Integer sender;         //발신
    private Integer total;          //합계
    private Integer totalCallTime;  //총통화시간
    private Integer avgCallTime;    //평균통화시간
}
