package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SendMessageTemplateFormRequest extends BaseForm {
    @NotNull("유형")
    private String categoryCode;
    @NotNull("메시지")
    private String content;
}
