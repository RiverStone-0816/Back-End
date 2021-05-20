package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConCodeFormRequest extends BaseForm {
    @NotNull("유형의 seq")
    private Integer type;
    @NotNull("필드 id값")
    private String fieldId;
    @NotNull("")
    private Integer conGroupId;
}
