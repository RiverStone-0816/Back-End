package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StatTotalRow<T> {
    private T timeInformation;  //날짜/시간

    private StatOutboundResponse outboundStat;
    private StatInboundResponse  inboundStat;

    //총 통화 건수
    @JsonIgnore
    public Integer getTotalCount() {
        return outboundStat.getSuccess() + inboundStat.getSuccess();
    }

    //총 통화 시간
    @JsonIgnore
    public Integer getTotalBillSec() {
        return outboundStat.getBillSecSum() + inboundStat.getBillSecSum();
    }

    public StatTotalRow(T timeInformation) {
        this.timeInformation = timeInformation;
    }
}
