package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class QueueMemberLoginCountResponse {
    private String queueName;
    private String isLogin;
    private Integer count;
}
