package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class CenterStatResponse {
    private Integer success = 0;
    private Integer connreq = 0;
    private Integer callback = 0;
    private Integer cancelCallback = 0;
}
