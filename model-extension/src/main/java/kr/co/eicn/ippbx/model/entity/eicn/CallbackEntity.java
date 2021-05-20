package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CallbackList;
import kr.co.eicn.ippbx.model.enums.CallbackStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackEntity extends CallbackList {
    private String svcName;
    private String huntName;
    private String idName;


    public boolean isStatusNone() {
        return CallbackStatus.NONE.getCode().equals(super.getStatus());
    }
}
