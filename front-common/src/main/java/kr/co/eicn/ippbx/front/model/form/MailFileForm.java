package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MailFileForm extends MailFileUpdateForm {
    @NotNull("유형")
    private String categoryCode;
}
