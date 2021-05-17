package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.PersonPausedStatus;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonitControlChangeRequest extends BaseForm {
    @NotNull("전화기아이디")
    private String peer;
    /**
     * @see PersonPausedStatus
     */
    @NotNull("상담원상태")
    private String paused;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (StringUtils.isNotEmpty(paused)){
            try {
                PersonPausedStatus.valueOf(paused);
            } catch (Exception e) {
                reject(bindingResult, "paused", "error.exception.status.412");
            }
        }

        return super.validate(bindingResult);
    }
}
