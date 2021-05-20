package kr.co.eicn.ippbx.front.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginForm extends BaseForm {
    @NotNull("회사 아이디")
    private String company;
    @NotNull("사용자 아이디")
    private String id;
    @NotNull("비밀번호")
    private String password;

    private String extension;

    private String authNum;
    private String actionType = "";
    private Boolean isChatLogin;
}
