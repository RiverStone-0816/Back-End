package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class DashResultCallChartResponse {
    private Integer inboundCnt;
    private Integer outboundCnt;
}
