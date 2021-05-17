package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class CenterStatusResponse {
    private Float rateValue = 0f;    //응대율
    private Integer waitCustomerCount = 0;  //고객 대기자수
    private Integer workingPerson = 0;      //근무상담사
    private Integer totalCallback = 0;      //총 콜백
    private Integer processedCallback = 0;    //처리 콜백
    private Integer unprocessedCallback = 0;//미처리 콜백
    private Integer waitPersonCount = 0;     //대기
    private Integer inboundCall = 0;        //I/B 통화중
    private Integer outboundCall = 0;       //O/B 통화중
    private Integer postProcess = 0;     //후처리
    private Integer etc = 0;                //기타
    private Integer loginCount = 0;         //로그인 상담원 수
    private Integer logoutCount = 0;        //로그아웃 상담원 수
}