package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class StatUserRankingResponse {
    private String idName;
    private Integer inSuccess;
    private Integer outSuccess;
    private Integer inBillsecSum;
    private Integer outBillsecSum;
    private Integer totalBillsecSum;
    private Integer totalSuccess;
    private Integer callbackSuccess;
}
