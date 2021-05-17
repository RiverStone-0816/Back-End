package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class DashServerMonitorResponse {
    private String title;
    private String usedCpu;
    private String usedMemory;
    private String usedHdd;
    private List<DashServerMonitorDaemonListResponse> daemonList;

}
