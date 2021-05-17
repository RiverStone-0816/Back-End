package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StatInboundTimeResponse<T> {
    private T timeInformation;

    private StatInboundResponse inboundStat;

    public StatInboundTimeResponse(T timeInformation) {
        this.timeInformation = timeInformation;
    }
}