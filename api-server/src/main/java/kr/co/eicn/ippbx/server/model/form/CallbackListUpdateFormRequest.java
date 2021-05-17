package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.CallbackStatus;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CallbackListUpdateFormRequest extends BaseForm {
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.CallbackStatus
     **/
    @NotNull("처리상태")
    private CallbackStatus status;

    @NotNull("서비스키값")
    private List<Integer> serviceSequences = new ArrayList<>();
}
