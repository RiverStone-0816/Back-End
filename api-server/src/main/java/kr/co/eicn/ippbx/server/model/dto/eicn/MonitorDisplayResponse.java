package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class MonitorDisplayResponse {
    private Integer total;      //인입호
    private Integer success;    //응대호
    private Integer cancel;     //포기호
    private Long  successAvg;     //응답률
}
