package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.IsWebVoiceYn;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebVoiceItemsDtmfFormRequest extends BaseForm {
    @NotNull("타이틀")
    private String dtmfTitle;
    @NotNull("자릿수")
    private String dtmfValue;
    @NotNull("보임여부")
    private String isView = IsWebVoiceYn.WEB_VOICE_N.getCode();

    @Override
    public boolean validate(BindingResult bindingResult) {
        return super.validate(bindingResult);
    }
}
