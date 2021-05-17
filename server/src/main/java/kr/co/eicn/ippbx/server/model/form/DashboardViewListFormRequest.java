package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

@Data
public class DashboardViewListFormRequest extends BaseForm {
    @NotNull("seq")
    private Integer seq;
    private Integer dashboardInfoId;
    private String companyId;
}
