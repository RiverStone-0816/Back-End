package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.enums.IdType;
import lombok.Data;

@Data
public class WebSecureHistoryResponse {
    private Integer seq;
    private String insertDate;
    private String secureIp;
    private String userId;
    private String userName;
    private IdType idType;
    private String extension;
    private String actionId;
    private String actionSubId;
    private String actionData;
}
