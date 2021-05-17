package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;

@Data
public class RAFormUpdateRequest extends BaseForm {
    @NotNull("유형")
    private String routeType;
    private String routeQueueNumber;
}
