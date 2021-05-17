package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class DashAveByServiceResponse {
    private String title;
    private List<DashServiceStatResponse> serviceListStat;  //대표서비스 통계
}
