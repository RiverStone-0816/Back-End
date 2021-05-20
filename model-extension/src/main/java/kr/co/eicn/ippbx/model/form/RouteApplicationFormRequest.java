package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.RouteApplicationType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteApplicationFormRequest extends BaseForm {
    @NotNull("유형")
    private RouteApplicationType type;
    @NotNull("신청번호")
    private String number;
    private String memo;
    private String uniqueId;
}
