package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StatOutboundTimeResponse<T> {
    private T timeInformation;

    private StatOutboundResponse statOutboundResponse;

    public StatOutboundTimeResponse(T timeInformation) {
        this.timeInformation = timeInformation;
    }
}