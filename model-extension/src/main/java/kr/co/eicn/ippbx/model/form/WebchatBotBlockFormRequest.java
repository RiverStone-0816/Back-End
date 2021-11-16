package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.IsTemplateEnable;
import lombok.Data;

@Data
public class WebchatBotBlockFormRequest {
    private String name;
    private String keyword;
    private IsTemplateEnable isTemplateEnable;
}
