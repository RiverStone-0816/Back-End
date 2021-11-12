package kr.co.eicn.ippbx.model.form;

import lombok.Data;

import java.util.List;

@Data
public class WebchatBotFormRequest {
    private String name;
    private String fallbackMent;
    private String fallbackAction;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;

    private BlockInfo blockInfo;

    @Data
    public static class BlockInfo {
        private String name;
        private String keyword;
        private String isTemplateEnable;
        private List<DisplayInfo> displayList;
        private List<ButtonElement> buttonList;
    }

    @Data
    public static class DisplayInfo {
        private Integer order;
        private String type;
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
        private String action;
        private Integer nextBlockId;
        private Integer nextGroupId;
        private String nextUrl;
        private String nextPhone;
        private String nextApiUrl;
        private String nextApiMent;
        private String isResultTemplateEnable;
        private String nextApiResultTemplate;
        private String nextApiNoResultMent;
        private String nextApiErrorMent;

        private List<ApiParam> paramList;
        private BlockInfo connectedBlockInfo;
    }

    @Data
    public static class ApiParam {
        private String type;
        private String paramName;
        private String displayName;
    }
}
