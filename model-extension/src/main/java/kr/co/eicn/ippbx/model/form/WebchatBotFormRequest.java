package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.ApiParameterType;
import kr.co.eicn.ippbx.model.enums.ButtonAction;
import kr.co.eicn.ippbx.model.enums.DisplayType;
import kr.co.eicn.ippbx.model.enums.FallbackAction;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WebchatBotFormRequest extends BaseForm {
    private String name;
    @NotNull("고객입력")
    private Boolean enableCustomerInput;
    private String fallbackMent;
    @NotNull("폴백액션")
    private FallbackAction fallbackAction;
    private Integer nextBlockId;
    private Integer nextGroupId;
    private String nextUrl;
    private String nextPhone;

    @Valid
    private BlockInfo blockInfo;

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class BlockInfo extends BaseForm {
        private Integer id; //실제 ID가 아닌 button 과 매핑할 수 있는 임의값
        private Integer posX;
        private Integer posY;
        private String name;
        private String keyword;
        private Boolean isTemplateEnable;
        @Valid
        private List<DisplayInfo> displayList;
        @Valid
        private List<ButtonElement> buttonList;
        @Valid
        private List<BlockInfo> children;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class DisplayInfo extends BaseForm {
        private Integer order;
        private DisplayType type;
        @Valid
        private List<DisplayElement> elementList;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class DisplayElement extends BaseForm {
        private Integer order;
        private String title;
        private String content;
        private String image;
        private String url;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ButtonElement extends BaseForm {
        private Integer order;
        private String buttonName;
        @NotNull("버튼액션")
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

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ApiParam extends BaseForm {
        @NotNull("API 파라미터 타입")
        private ApiParameterType type;
        private String paramName;
        private String displayName;
    }
}
