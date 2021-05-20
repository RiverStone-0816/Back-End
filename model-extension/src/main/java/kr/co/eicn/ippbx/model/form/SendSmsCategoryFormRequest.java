package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.util.valid.Length;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class SendSmsCategoryFormRequest extends BaseForm {
    @NotNull("카테고리 코드")
    @Length(max = 4)
    private String categoryCode;
    @NotNull("카테고리 명")
    private String categoryName;
    @NotNull("카테고리 분류(S:SMS)")
    private String categoryType;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if(isNotEmpty(categoryCode)) {
            if(!SendCategoryType.SMS.getCode().equals(categoryCode.substring(0,1))) {
                reject(bindingResult, "categoryCode", "{카테고리 코드가 S로 시작하지 않습니다.}");
            }
        }
        if(isNotEmpty(categoryType)) {
            if(!SendCategoryType.SMS.getCode().equals(categoryType)) {
                reject(bindingResult, "categoryType", "{카테고리 분류에 맞게 입력해주세요.(S:SMS)");
            }
            if(!categoryCode.substring(0,1).equals(categoryType)) {
                reject(bindingResult, "categoryType", "{카테고리 분류가 올바르지 않습니다}");
            }
        }
        return super.validate(bindingResult);
    }
}
