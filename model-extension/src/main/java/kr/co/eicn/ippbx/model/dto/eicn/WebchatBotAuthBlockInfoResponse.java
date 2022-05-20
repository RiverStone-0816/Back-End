package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.AuthButtonAction;
import kr.co.eicn.ippbx.model.enums.AuthDisplayType;
import lombok.Data;

import java.util.List;

@Data
public class WebchatBotAuthBlockInfoResponse {
    private Integer id;
    private Integer botId;
    private String name;
    private Boolean usingOtherBot;
    private List<AuthParamInfo> params;
    private List<AuthButtonInfo> buttons;

    @Data
    public static class AuthParamInfo {
        private Integer id;
        private Integer blockId;
        private Integer sequence;
        private AuthDisplayType type;
        private String title;
        private String name;
        private String paramName;
        private Boolean needYn;
    }

    @Data
    public static class AuthButtonInfo {
        private Integer id;
        private Integer blockId;
        private Integer sequence;
        private String name;
        private AuthButtonAction action;
        private String actionData;
        private String resultParamName;
    }
}
