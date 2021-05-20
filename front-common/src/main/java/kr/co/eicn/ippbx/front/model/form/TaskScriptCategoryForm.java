package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskScriptCategoryForm extends BaseForm {
    private Map<Long, String> modifyingCategoryIdToNameMap = new HashMap<>();
    private List<String> addingCategoryNames = new ArrayList<>();
    private List<Long> deletingCategoryIds = new ArrayList<>();

    @Override
    public boolean validate(BindingResult bindingResult) {
        modifyingCategoryIdToNameMap.forEach((id, name) -> {
            if (id == null)
                reject(bindingResult, "modifyingCategoryIdToNameMap", "validator.blank", "카테고리 아이디");
            if (StringUtils.isEmpty(name))
                reject(bindingResult, "modifyingCategoryIdToNameMap[" + id + "]", "validator.blank", "카테고리 이름");
        });

        for (int i = 0; i < addingCategoryNames.size(); i++)
            if (StringUtils.isEmpty(addingCategoryNames.get(i)))
                reject(bindingResult, "addingCategoryNames[" + i + "]", "validator.blank", "카테고리 이름");

        for (int i = 0; i < deletingCategoryIds.size(); i++)
            if (deletingCategoryIds.get(i) == null)
                reject(bindingResult, "deletingCategoryIds[" + i + "]", "validator.blank", "카테고리 아이디");

        return super.validate(bindingResult);
    }
}
