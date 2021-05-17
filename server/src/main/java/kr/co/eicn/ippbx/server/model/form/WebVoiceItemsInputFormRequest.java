package kr.co.eicn.ippbx.server.model.form;

import kr.co.eicn.ippbx.server.model.enums.IsWebVoiceYn;
import kr.co.eicn.ippbx.server.util.valid.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebVoiceItemsInputFormRequest {
    private String inputTitle;
    private Integer maxLen;
    @NotNull("보임여부")
    private String isView = IsWebVoiceYn.WEB_VOICE_N.getCode();
}
