package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.RouteType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class GradeListFormRequest extends BaseForm {
    @NotNull("전화번호")
    private String gradeNumber;
    private String grade;
    /**
     * @see kr.co.eicn.ippbx.model.enums.RouteType
     */
    @NotNull("인입 방법")
    private String type;
    private String queueNumber;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(type) && Objects.equals(RouteType.HUNT.getCode(), type)) {
            if (isEmpty(queueNumber))
                reject(bindingResult, "queueNumber", "{큐번호를 선택하여 주세요.}", "queueNumber");
        }
        return super.validate(bindingResult);
    }
}
