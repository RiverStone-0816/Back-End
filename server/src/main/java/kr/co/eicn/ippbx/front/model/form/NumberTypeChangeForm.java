package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.front.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.NumberType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NumberTypeChangeForm extends BaseForm {
    private NumberType type;
}
