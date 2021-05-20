package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DashboardViewListFormRequest extends BaseForm {
    @NotNull("seq")
    private Integer seq;
    private Integer dashboardInfoId;
    private String companyId;
}
