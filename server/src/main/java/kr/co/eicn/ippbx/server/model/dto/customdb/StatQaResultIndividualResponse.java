package kr.co.eicn.ippbx.server.model.dto.customdb;

import lombok.Data;

import java.util.List;

@Data
public class StatQaResultIndividualResponse {
    private Integer seq;
    private String codeId;
    private String codeName;

    private List<QaResultResponse> qaResultStat;
}
