package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

@EqualsAndHashCode(callSuper = true)
@Data
public class GradeFileForm extends FileForm {
    @NotNull("등급유형")
    private String gradeType;

    public void validate(String prefix, BindingResult bindingResult) {
        if (StringUtils.isEmpty(gradeType))
            reject(bindingResult, prefix + "gradeType", "validator.blank", "등급유형");
    }
}
