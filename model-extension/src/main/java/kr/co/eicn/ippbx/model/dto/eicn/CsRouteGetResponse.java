package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class CsRouteGetResponse {
    private Integer seq;
    private String  queueNumber;
    private String  cycle;
}
