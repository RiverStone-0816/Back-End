package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VipRouteFormRequest extends BaseForm {
    @NotNull("등록할전화번호")
    private String vipNumber;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.RouteType;
     */
    @NotNull("유형타입")
    private String type;
    private String huntNumber;

    //private String vipName;
    //private String etc;
}
