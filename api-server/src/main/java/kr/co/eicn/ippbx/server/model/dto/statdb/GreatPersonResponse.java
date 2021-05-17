package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class GreatPersonResponse {
    private String type;
    private String idName;
    private Integer callCount;
    private Integer callTime;
}
