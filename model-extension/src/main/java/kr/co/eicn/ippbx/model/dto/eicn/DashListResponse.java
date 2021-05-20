package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.DashboardInfoDashboardType;
import lombok.Data;

@Data
public class DashListResponse {
    private Integer dashboardId;
    private String dashboardName;
    private DashboardInfoDashboardType dashboardType;
    private String dashboardValue;
    private Integer dashboardUiSeq;
}
