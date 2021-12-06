package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebchatServiceInfoFormRequest extends BaseForm {
    private String channelName;
    @NotNull("서비스키")
    private String senderKey;
    private Boolean enableChat;
    private String displayCompanyName;
    private String message;
    private String image;
    private String backgroundColor = "#ffffff";
    private String profile;
}
