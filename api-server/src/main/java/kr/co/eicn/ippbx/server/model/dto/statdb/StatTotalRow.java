package kr.co.eicn.ippbx.server.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StatTotalRow<T> {
    private T timeInformation;

    @JsonIgnore
    public Integer getTotalCount() {
        return outboundStat.getSuccess() + inboundStat.getSuccess();
    }

    @JsonIgnore
    public Integer getTotalBillSec() {
        return outboundStat.getBillSecSum() + inboundStat.getBillSecSum();
    }

    private StatOutboundResponse outboundStat;
    private StatInboundResponse inboundStat;

    public StatTotalRow(T timeInformation) {
        this.timeInformation = timeInformation;
    }
}