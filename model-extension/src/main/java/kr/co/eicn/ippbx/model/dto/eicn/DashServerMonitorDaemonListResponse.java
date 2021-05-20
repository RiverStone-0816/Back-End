package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class DashServerMonitorDaemonListResponse {
    private String daemonName;
    /**
     * @see kr.co.eicn.ippbx.model.enums.DaemonStatus
     *start
     */
    private String daemonStatus;
}
