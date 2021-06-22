package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class StatMyCallResponse {
    private Integer inTotal = 0;
    private Integer inSuccess = 0;
    private Integer callback = 0;
    private Integer outSuccess = 0;
    private Double successPer;
}
