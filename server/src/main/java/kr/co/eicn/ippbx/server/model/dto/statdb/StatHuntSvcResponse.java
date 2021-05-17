package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class StatHuntSvcResponse {
    private String queueName;
    private List<StatHuntInboundResponse> inboundResponse;
}
