package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonCodeFormRequest extends BaseForm {
    private String codeId;
    private String codeName;
    private Integer sequence;
    private String hide;
    private String script;
}
