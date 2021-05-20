package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.meta.jooq.eicn.enums.ScreenConfigExpressionType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScreenConfigFormRequest extends BaseForm {
    @NotNull("전광판 명칭")
    private String name;
    @NotNull("디자인")
    private Integer lookAndFeel;
    @NotNull("표시데이터")
    private ScreenConfigExpressionType expressionType;
    private Boolean showSlidingText = false;
    private String slidingText;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (showSlidingText != null && showSlidingText && StringUtils.isEmpty(slidingText))
            reject(bindingResult, "slidingText", "슬라이딩 문구를 입력하여 주세요.", "슬라이딩 문구");

        return super.validate(bindingResult);
    }
}
