package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.WebVoiceInfoYn;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebVoiceInfoFormRequest extends BaseForm {
    private String context;
    private Integer ivrCode;
    private String uiType;
    private String bannerUrl;
    private String bannerImgFile;
    @NotNull("헤더영역 사용여부")
    private String headerUse = WebVoiceInfoYn.UNUSED.getCode();
    @NotNull("텍스트영역 사용여부")
    private String textareaUse = WebVoiceInfoYn.UNUSED.getCode();
    @NotNull("입력영역 사용여부")
    private String inputareaUse = WebVoiceInfoYn.UNUSED.getCode();
    @NotNull("컨트롤영역 사용여부")
    private String controlUse = WebVoiceInfoYn.UNUSED.getCode();
    private String tailUse = WebVoiceInfoYn.UNUSED.getCode();
    private String companyId;
}
