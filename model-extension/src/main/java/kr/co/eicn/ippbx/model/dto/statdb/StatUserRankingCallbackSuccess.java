package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class StatUserRankingCallbackSuccess {
    private String id;
    private String idName;
    private Integer callbackSuccess;
}
