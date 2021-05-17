package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

@Data
public class SendMessageTemplateUpdateRequest extends BaseForm {
    @NotNull("카테고리 코드")
    private String categoryCode;
    @NotNull("메시지")
    private String content;
}