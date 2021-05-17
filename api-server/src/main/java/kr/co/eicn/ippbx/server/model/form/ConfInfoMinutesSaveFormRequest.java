package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfInfoMinutesSaveFormRequest extends BaseForm {
    @NotNull("시퀀스")
    private Integer confId;
    @NotNull("회의록")
    private String confMinute;
}