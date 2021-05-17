package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DisplayMemberStatusListResponse {
    private String  statusName;     //상태명
    private Integer statusNumber;   //상태 번호
    private Integer cnt = 0;    //갯수
}
