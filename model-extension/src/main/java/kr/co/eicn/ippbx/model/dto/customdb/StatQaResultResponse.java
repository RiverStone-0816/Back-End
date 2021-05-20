package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.util.List;

@Data
public class StatQaResultResponse {
    private Integer seq;
    private String name;

    private List<StatQaResultFieldResponse> fieldResponses;
}
