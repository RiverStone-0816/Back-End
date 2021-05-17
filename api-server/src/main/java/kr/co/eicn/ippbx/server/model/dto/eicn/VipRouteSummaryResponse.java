package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class VipRouteSummaryResponse {
    private Integer seq;
    private String vipNumber;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums;
     */
    private String type; //유형
    private String queueNumber;
    private String queueName;
}
