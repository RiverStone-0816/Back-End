package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class DashQueueMemberResponse {
    private String peer;
    private Integer status;  //상태
    private boolean login; //로그인여부

    private String queueNumber;
    private String inOut;
    private String src;
    private String dst;
}
