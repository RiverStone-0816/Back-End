package kr.co.eicn.ippbx.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequest extends BaseForm {
    @Schema(description = "회사아이디", example = "globalassistance")
    @NotNull("회사아아디")
    private String company;

    @Schema(description = "아이디", example = " ")
    @NotNull("아이디")
    private String id;

    @Schema(description = "비밀번호", example = " ")
    private String password;

    @Schema(description = "내선번호", example = " ")
    private String extension;

    @Schema(hidden = true, description = "actionType", example = " ")
    private String actionType = "";

    @Schema(hidden = true, description = "isChatLogin", example = "false")
    private Boolean isChatLogin;
}
