package kr.co.eicn.ippbx.model.form;

import lombok.Data;

@Data
public class WebchatBotDisplayElementFormRequest {
    private Integer displayId;
    private Integer order;
    private String title;
    private String content;
    private String image;
    private String url;
}
