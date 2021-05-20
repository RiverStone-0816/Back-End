package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.WebVoiceInfoYn;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebVoiceItemsFormRequest extends WebVoiceInfoFormRequest {
    private String headerStr;
    private String textStr;
    @Valid
    private List<WebVoiceItemsInputFormRequest> titles;
    @Valid
    private List<WebVoiceItemsDtmfFormRequest> dtmfs;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (Objects.equals(getHeaderUse(), WebVoiceInfoYn.USE.getCode()) && StringUtils.isEmpty(headerStr))
            reject(bindingResult, "headerStr", "messages.validator.blank", "헤더문구");

        if (Objects.equals(getTextareaUse(), WebVoiceInfoYn.USE.getCode()) && StringUtils.isEmpty(textStr))
            reject(bindingResult, "textStr", "messages.validator.blank", "텍스트");

        return super.validate(bindingResult);
    }
}
