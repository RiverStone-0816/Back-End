package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

@Data
public class ConGroupFormRequest extends BaseForm {
    @NotNull("그룹명")
    private String name;
    @NotNull("고객정보유형")
    private Integer conType;
    private String info;
    private String groupCode;
}
