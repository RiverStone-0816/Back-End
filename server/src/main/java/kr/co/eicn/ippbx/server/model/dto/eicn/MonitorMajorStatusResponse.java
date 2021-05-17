package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class MonitorMajorStatusResponse {
    private String number;
    private Integer total = 0;
    private Integer connReq = 0;
    private Integer success = 0;
    private Integer onlyread = 0;
    private Integer cancel = 0;
    private Integer callback = 0;
    private Integer callbackSuccess = 0;
    private Integer consultation = 0;
    private Integer totalUser = 0;
    private Integer loginUser = 0;
}
