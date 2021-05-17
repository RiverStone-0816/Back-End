package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class DashServerMonitorDaemonListResponse {
    private String daemonName;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.DaemonStatus
     *start
     */
    private String daemonStatus;
}
