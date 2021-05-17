package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DisplayHuntMemberListResponse {
    private Integer paused;  //상담원상태
    private String memberName; //상담원 peer
    private String isLogin;    //로그인상태(Y,N)
}
