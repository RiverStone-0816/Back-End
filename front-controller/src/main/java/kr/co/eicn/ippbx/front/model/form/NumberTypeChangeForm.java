package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.model.enums.NumberType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NumberTypeChangeForm extends BaseForm {
    private NumberType type;
}
