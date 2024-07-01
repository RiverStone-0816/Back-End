package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginHistoryFormRequest extends BaseForm {
    private String loginDate;
    private String logoutDate;
    @NotBlank
    private String userId;
    @NotEmpty
    private String userName;
    private IdType idType;
    private String extension;
    private String partGroup;
    private String partName;
    private Byte logoutStatus;
    private Byte dialStatus;
    private String companyId;
}
