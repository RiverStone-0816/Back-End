package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebSecureHistoryFormRequest extends BaseForm {
    @NotBlank
    private String insertDate;
    @NotBlank
    private String secureSessionId;
    @NotBlank
    private String secureIp;
    private String userId;
    private String userName;
    private IdType idType;
    private String extension;
    @NotBlank
    private String actionId;
    @NotBlank
    private String actionSubId;
    @NotBlank
    private String actionData;
    @NotBlank
    private String companyId;
}
