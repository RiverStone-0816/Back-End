package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DashExcellentCSListResponse {
    private String userName; //상담원명
    private Integer userScore;  //실적
}
