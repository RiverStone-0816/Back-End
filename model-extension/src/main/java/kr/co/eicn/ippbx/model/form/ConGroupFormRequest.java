package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConGroupFormRequest extends BaseForm {
    @NotNull("그룹명")
    private String name;
    @NotNull("고객정보유형")
    private Integer conType;
    private String info;
    private String groupCode;
}
