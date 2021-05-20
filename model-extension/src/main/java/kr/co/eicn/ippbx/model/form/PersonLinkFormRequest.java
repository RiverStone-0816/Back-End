package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.Length;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonLinkFormRequest extends BaseForm {
    @NotNull("이름")
    @Length(min = 1, max = 50, value = "이름")
    private String name;

    @NotNull("링크주소")
    @Length(min = 1, max = 500, value = "링크주소")
    private String reference;
}
