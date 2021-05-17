package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class PDSQueuePersonResponse {
    private String id;
    private String paused;
    private String queueName;
}
