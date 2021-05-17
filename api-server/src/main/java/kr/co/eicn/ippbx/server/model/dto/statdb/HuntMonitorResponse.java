package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class HuntMonitorResponse {
    private String queueName;            //큐명
    private String queueHanName;            //큐 한글명
    private Integer customWait = 0;     //대기고객
    private Map<Integer, Integer> statusCountMap = new LinkedHashMap<>();
    private Integer loginCount = 0;     //로그인수
    private Integer logoutCount = 0;    //로그아웃수
    private Integer total = 0;          //총합
}