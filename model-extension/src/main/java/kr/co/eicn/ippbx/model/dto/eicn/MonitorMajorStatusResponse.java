package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class MonitorMajorStatusResponse {
    private String number;
    private Integer total = 0;
    private Integer connreq = 0;
    private Integer success = 0;
    private Integer onlyread = 0;
    private Integer cancel = 0;
    private Integer callback = 0;
    private Integer callbackSuccess = 0;
    private Integer loginUserCnt = 0;
    private Integer waitPersonCnt = 0;
    private Integer workingPersonCnt = 0;
}
