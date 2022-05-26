package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebchatBotInfoResponse extends WebchatBotFallbackInfoResponse {
    private Integer id;
    private String name;
    private BlockInfo blockInfo;

    @Valid
    private List<WebchatBotFormBlockInfoResponse> authBlockList;

    @Data
    public static class BlockInfo {
        private Integer id;
        private Integer parentButtonId;
        private Integer posX;
        private Integer posY;
        private String name;
        private String keyword;
        private BlockType type;
        private Integer formBlockId;
        private Boolean isTemplateEnable;

        private List<DisplayInfo> displayList;
        private List<ButtonInfo> buttonList;
        private List<BlockInfo> children;
        private List<AuthResultElement> authResultElementList;
    }

    @Data
    public static class DisplayInfo {
        private Integer id;
        private Integer blockId;
        private Integer order;
        private DisplayType type;
        private List<DisplayElement> elementList;
    }

    @Data
    public static class DisplayElement {
        private Integer id;
        private Integer displayId;
        private Integer order;
        private String title;
        private String content;
        private String image;
        private String url;
        private DisplayElementInputType inputType;
        private String paramName;
        private String displayName;
        private String needYn;
    }

    @Data
    public static class ButtonInfo {
        private Integer id;
        private Integer blockId;
        private Integer order;
        private String name;
        private ButtonAction action;
        private Integer nextBlockId;
        private Integer nextGroupId;
        private String nextUrl;
        private String nextPhone;
        private String nextApiUrl;
        private String nextApiMent;
        private Boolean isResultTemplateEnable;
        private String nextApiResultTemplate;
        private String nextApiNoResultMent;
        private String nextApiErrorMent;
    }

    @Data
    public static class AuthResultElement {
        private Integer id;
        private Integer blockId;
        private String value;
        private String ment;
        private ButtonAction action;
        private String nextActionData;
        private String nextApiMent;
        private Boolean enableResultTemplate;
        private String nextApiResultTemplate;
        private String nextApiNoResultMent;
        private String nextApiErrorMent;
    }
}
