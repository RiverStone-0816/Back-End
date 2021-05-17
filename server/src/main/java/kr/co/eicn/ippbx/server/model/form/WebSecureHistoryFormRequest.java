package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.util.spring.BaseForm;

import kr.co.eicn.ippbx.server.model.enums.IdType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
