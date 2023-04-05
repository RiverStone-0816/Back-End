package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class StatUserResponse<T> {
    private T timeInformation;
    private List<UserStat> userStatList;

    public StatUserResponse(T timeInformation) {
        this.timeInformation = timeInformation;
    }

    @Data
    public static class UserStat {
        private String userId;
        private String idName;
        private String groupName;
        private String groupCode;
        private String groupTreeName;
        private Integer totalCnt;
        private Integer totalSuccess;
        private Integer totalBillSec;

        private StatUserOutboundResponse outboundStat;
        private StatUserInboundResponse inboundStat;
        private StatMemberStatusResponse memberStatusStat;

        @JsonIgnore
        public void setTotalCnt() {
            this.totalCnt = outboundStat.getOutTotal() + inboundStat.getTotal();
        }

        @JsonIgnore
        public void setTotalBillSec() {
            this.totalBillSec = outboundStat.getOutBillSecSum() + inboundStat.getBillSecSum();
        }

        public Integer getTotalSuccess() {
            return outboundStat.getOutSuccess() + inboundStat.getSuccess();
        }
    }
}
