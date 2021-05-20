package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

import java.util.List;

@Data
public class StatHuntSvcResponse {
    private String queueName;
    private List<StatHuntInboundResponse> inboundResponse;
}
