package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class FieldCodeResponse {
    private Integer type;
    private String  fieldId;
    private String  fieldInfo;
}
