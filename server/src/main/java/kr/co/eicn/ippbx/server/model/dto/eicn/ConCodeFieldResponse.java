package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class ConCodeFieldResponse {
    private String fieldId;
    private String fieldInfo;
    private Integer conGroupId;
}
