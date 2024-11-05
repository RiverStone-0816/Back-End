package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StatOutboundTimeResponse<T> {
    private T timeInformation;  //날짜/시간

    private StatOutboundResponse statOutboundResponse;

    public StatOutboundTimeResponse(T timeInformation) {
        this.timeInformation = timeInformation;
    }
}
