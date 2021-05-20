package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class ConCodeResponse {
    private Integer seq;
    private String name;
    private String kind;

    private List<ConCodeFieldResponse> conFields;
}
