package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;
import kr.co.eicn.ippbx.server.model.enums.IdType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
