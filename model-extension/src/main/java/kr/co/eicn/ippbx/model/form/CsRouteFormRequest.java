package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.containsWhitespace;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class CsRouteFormRequest extends BaseForm {
    @NotNull("큐번호")
    private String  queueNumber;
    @NotNull("검색주기")
    private String  cycle;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(queueNumber))
            if (containsWhitespace(queueNumber))
                reject(bindingResult, "huntNumber", "{빈 공백문자를 포함할 수 없습니다.}", "");
        if (isNotEmpty(cycle))
            if (containsWhitespace(cycle))
                reject(bindingResult, "cycle", "{빈 공백문자를 포함할 수 없습니다.}", "");

        return super.validate(bindingResult);
    }

}
