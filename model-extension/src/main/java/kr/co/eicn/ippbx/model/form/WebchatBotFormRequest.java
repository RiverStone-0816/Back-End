package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.*;
import lombok.Data;

import java.util.List;

@Data
public class WebchatBotFormRequest {
    private String name;
    private String fallbackMent;
    private FallbackAction fallbackAction;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;

    private BlockInfo blockInfo;

    @Data
    public static class BlockInfo {
        private Integer id; //실제 ID가 아닌 button 과 매핑할 수 있는 임의값
        private Integer posX;
        private Integer posY;
        private String name;
        private String keyword;
        private Boolean isTemplateEnable;
        private List<DisplayInfo> displayList;
        private List<ButtonElement> buttonList;
        private List<BlockInfo> children;
    }

    @Data
    public static class DisplayInfo {
        private Integer order;
        private DisplayType type;
        private List<DisplayElement> elementList;
    }

    @Data
    public static class DisplayElement {
        private Integer order;
        private String title;
        private String content;
        private String image;
        private String url;
    }

    @Data
    public static class ButtonElement {
        private Integer order;
        private String buttonName;
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

        private List<ApiParam> paramList;
    }

    @Data
    public static class ApiParam {
        private ApiParameterType type;
        private String paramName;
        private String displayName;
    }
}
