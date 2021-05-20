package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class DashMonitorByHuntResponse {
    private String title;
    private List<DashHuntMonitorResponse> huntList;  //헌트별 모니터링 리스트
}
