package kr.co.eicn.ippbx.model.form;

import lombok.Data;

import java.util.List;

@Data
public class CommonCodeUpdateFormRequest {
    private String relatedFieldId;

    private List<CommonCodeFormRequest> codes;
}
