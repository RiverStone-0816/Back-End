package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.model.enums.DupKeyKind;
import kr.co.eicn.ippbx.server.model.enums.IsDupNeedYn;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaindbGroupFormRequest extends MaindbGroupUpdateRequest {
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.IsDupNeedYn
     */
    @NotNull("데이터 중복 체크여부")
    private String isDupUse = IsDupNeedYn.USE.getCode();
    private String dupIsUpdate = "Y"; // 업로드시 중복처리 방법
    private String dupNeedField;    //필수항목
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.DupKeyKind
     */
    private String dupKeyKind = DupKeyKind.FIELD_NUMBER.getCode(); // 중복 체크 항목
    private String info;
    private String groupCode;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(isDupUse)) {
            if (IsDupNeedYn.USE.getCode().equals(isDupUse)) {
                if (isEmpty(dupIsUpdate))
                    reject(bindingResult, "dupIsUpdate", "{중복체크할 필수 항목 선택후 사용.}");
                if (isEmpty(dupKeyKind))
                    reject(bindingResult, "dupKeyKind", "{중복체크할 필수 항목 선택후 사용.}");
                if (isNotEmpty(dupKeyKind))
                    if (DupKeyKind.FIELD_NUMBER.getCode().equals(dupKeyKind) || DupKeyKind.FIELD.getCode().equals(dupKeyKind))
                        if (isEmpty(dupNeedField))
                            reject(bindingResult, "dupNeedField", "{중복체크할 필수 항목 선택후 사용.}");
            }
        }

        return super.validate(bindingResult);
    }
}
