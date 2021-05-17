package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class PersonStatusResponse {
    private String phone;       //전화기상태 (정상/비정상)
    private String isLogin;     //로그인상태
    private String queue;        //인입헌트
    private String status;      //상태
    private Integer keepTime;   //유지시간
    private String sendReceive; //수/발신
    private String number = ""; //고객번호
}
