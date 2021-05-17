package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfMemberOutPersonFormRequest extends BaseForm {
    @NotNull("참가자명")
    private String memberName;
    @NotNull("전화번호")
    private String memberNumber;

}