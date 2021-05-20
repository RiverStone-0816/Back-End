package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class DashAveByServiceListResponse {
    private String svcName; //대표서비스 명
    private float svcAve;  //대표서비스 응답률
}
