package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.IntroChannelType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebchatServiceInfoResponse extends WebchatServiceSummaryInfoResponse {
    private String message;
    private String imageFileName;
    private String backgroundColor;
    private String profileFileName;
    private List<IntroChannel> introChannelList;

    @Data
    public static class IntroChannel {
        private Integer id;
        private Integer introId;
        private IntroChannelType channelType;
        private String channelId;
    }
}
