package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.IntroChannelType;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<IntroChannel> introChannelList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class IntroChannel {
        private String id;
        private IntroChannelType type;
    }
}
