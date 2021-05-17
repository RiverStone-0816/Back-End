package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class StatHuntResponse<T> {
    private T timeInformation;
    private List<StatHuntInboundResponse> statQueueInboundResponses;

    public StatHuntResponse(T timeInformation) {
        this.timeInformation = timeInformation;
    }
}