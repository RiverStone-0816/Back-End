package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class DashQueueMemberResponse {
    private String peer;
    private Integer status;  //상태
    private boolean login; //로그인여부
}