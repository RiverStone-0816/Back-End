package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class RouteApplicationSummaryResponse {
    private Integer   seq;
    private String inputDate;
    private String    type;
    private String    number;
    private String    appUserid;
    private String    appUsername;
    private String    rstUserid;
    private String    rstUsername;
    private String    memo;
    /**
     * @see kr.co.eicn.ippbx.server.util.CodeHasable;
     */
    private String    result;//처리결과
    private String    recordFile;
}
