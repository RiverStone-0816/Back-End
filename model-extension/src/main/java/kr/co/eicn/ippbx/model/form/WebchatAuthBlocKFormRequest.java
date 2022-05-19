package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.AuthButtonAction;
import kr.co.eicn.ippbx.model.enums.AuthDisplayType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebchatAuthBlocKFormRequest extends BaseForm {
    private String name;
    private String title;
    private Boolean usingOtherBot;
    private List<AuthParamInfo> params;
    private List<AuthButtonInfo> buttons;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class AuthParamInfo extends BaseForm {
        private AuthDisplayType type;
        private String name;
        private String paramName;
        private Boolean needYn;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class AuthButtonInfo extends BaseForm {
        private String name;
        private AuthButtonAction action;
        private String actionData;
        private String resultParamName;
    }
}
