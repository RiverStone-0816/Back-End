package kr.co.eicn.ippbx.server.model.dto.customdb;

import lombok.Data;

import java.util.List;

@Data
public class StatQaResultFieldResponse {
    private String fieldId;
    private String fieldName;
    private String fieldInfo;

    private List<StatQaResultCodeResponse> codeResponses;
}