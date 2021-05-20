package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class VipRouteSummaryResponse {
    private Integer seq;
    private String vipNumber;
    /**
     * @see kr.co.eicn.ippbx.model.enums;
     */
    private String type; //유형
    private String queueNumber;
    private String queueName;
}
