package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.DisplayElementInputType;
import lombok.Data;

@Data
public class WebchatBotDisplayElementFormRequest {
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
