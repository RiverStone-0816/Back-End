package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class SendFaxEmailCategoryFormRequest extends BaseForm {
    @NotNull("카테고리 코드")
    private String categoryCode;
    @NotNull("카테고리 명")
    private String categoryName;
    @NotNull("카테고리 분류(F:FAX, E:EMAIL)")
    private String categoryType;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if(isNotEmpty(categoryCode)) {
            if(!SendCategoryType.FAX.getCode().equals(categoryCode.substring(0,1))) {
                if(!SendCategoryType.EMAIL.getCode().equals(categoryCode.substring(0,1))) {
                    reject(bindingResult, "categoryCode", "{카테고리 코드가 F 또는 E로 시작하지 않습니다.}");
                }
            }
        }
        if(isNotEmpty(categoryType)) {
            if(!SendCategoryType.FAX.getCode().equals(categoryType)) {
                if(!SendCategoryType.EMAIL.getCode().equals(categoryType)) {
                    reject(bindingResult, "categoryType", "{카테고리 분류에 맞게 입력해주세요.(F:FAX, E:EMAIL)}");
                }
            }
            if(!categoryType.equals(categoryCode.substring(0,1))) {
                reject(bindingResult, "categoryType", "{카테고리 분류가 올바르지 않습니다}");
            }
        }
        return super.validate(bindingResult);
    }
}
