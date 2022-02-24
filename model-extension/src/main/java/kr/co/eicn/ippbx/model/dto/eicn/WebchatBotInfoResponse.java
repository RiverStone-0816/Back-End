package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.enums.DisplayElementInputType;
import kr.co.eicn.ippbx.model.enums.DisplayType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebchatBotInfoResponse extends WebchatBotFallbackInfoResponse {
    private Integer id;
    private String name;
    private BlockInfo blockInfo;

    @Data
    public static class BlockInfo {
        private Integer id;
        private Integer parentButtonId;
        private Integer posX;
        private Integer posY;
        private String name;
        private String keyword;
        private Boolean isTemplateEnable;

        private List<DisplayInfo> displayList;
        private List<ButtonInfo> buttonList;
        private List<BlockInfo> children;
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
}
