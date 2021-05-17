package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class CsRouteSummaryResponse {
    private Integer seq;
    private String  queueNumber;
    private String  queueName;
    private String  cycle;
}
