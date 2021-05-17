package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.model.enums.GroupLevelAuth;
import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Data
public class MenuFormRequest extends BaseForm {
    @NotNull("메뉴명")
    private String menuName;
    @NotNull("보임여부")
    private Boolean view;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.GroupLevelAuth
     */
    private String groupLevelAuth;
    private String groupCode;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(groupLevelAuth)) {
            if (groupLevelAuth.contains(GroupLevelAuth.ALLOW_ONLY_AUTHORIZED_ORGANIZATIONS.getCode()) && StringUtils.isEmpty(groupCode))
                reject(bindingResult, "groupCode", "{조직을 선택 하여 주세요.}", "");
        }
        
        return super.validate(bindingResult);
    }
}
