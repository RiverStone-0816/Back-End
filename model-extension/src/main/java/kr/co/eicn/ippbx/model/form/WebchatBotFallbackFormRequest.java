package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.FallbackAction;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebchatBotFallbackFormRequest extends BaseForm {
    @NotNull("고객입력")
    private Boolean enableCustomerInput;
    private String fallbackMent;
    @NotNull("폴백액션")
    private FallbackAction fallbackAction;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;
}
