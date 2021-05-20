package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatQaResultIndividualResponse {
    private Integer seq;
    private String codeId;
    private String codeName;

    private List<QaResultResponse> qaResultStat = new ArrayList<>();
}
