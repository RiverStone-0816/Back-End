package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SendMessageTemplateUpdateRequest extends BaseForm {
    @NotNull("카테고리 코드")
    private String categoryCode;
    @NotNull("메시지")
    private String content;
}
