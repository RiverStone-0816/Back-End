package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationWholeCategoryFormRequest extends BaseForm {
    @Valid
    @NotNull("카테고리")
    private List<EvaluationCategoryFormRequest> categories = new ArrayList<>();

    @Override
    public boolean validate(BindingResult bindingResult) {
        int maxScore = categories.stream().mapToInt(e -> e.getItems().stream().mapToInt(EvaluationItemFormRequest::getMaxScore).sum()).sum();
        if (maxScore != 100)
            reject(bindingResult, "categories", "{총배점은 100점이어야 합니다.}");

        return super.validate(bindingResult);
    }
}
