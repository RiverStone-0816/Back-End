package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkTemplateFormRequest extends BaseForm {

    /**
     * @see kr.co.eicn.ippbx.server.model.enums.TalkTemplate;
     */
    @NotNull("템플릿 타입")
    private String  type;
    @NotNull("템플릿 유형 데이터")
    private String  typeData;
    @NotNull("템플릿명")
    private String  mentName;
    @NotNull("템플릿 멘트")
    private String  ment;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(type))
            if (containsWhitespace(type))
                reject(bindingResult, "type", "{빈 공백문자를 포함할 수 없습니다.}", "");

        return super.validate(bindingResult);
    }
}
