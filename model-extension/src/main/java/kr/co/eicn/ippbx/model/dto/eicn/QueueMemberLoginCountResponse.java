package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class QueueMemberLoginCountResponse {
    private String queueName;
    private String isLogin;
    private Integer count;
}
