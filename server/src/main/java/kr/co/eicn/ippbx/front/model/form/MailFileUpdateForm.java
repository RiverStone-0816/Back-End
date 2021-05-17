package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MailFileUpdateForm extends FileForm {
    @NotNull("발송물명")
    private String name;
    @NotNull("발송물 설명")
    private String desc;
}
