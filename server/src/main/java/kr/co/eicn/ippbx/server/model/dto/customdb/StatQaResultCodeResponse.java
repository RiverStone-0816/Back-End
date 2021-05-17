package kr.co.eicn.ippbx.server.model.dto.customdb;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatQaResultCodeResponse {
    private String codeId;
    private String codeName;

    private List<QaResultResponse> qaResultStat = new ArrayList<>();
}
