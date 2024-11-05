package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class StatUserResponse<T> {
    private T timeInformation;  //날짜/시간

    private List<UserStat> userStatList;

    public StatUserResponse(T timeInformation) {
        this.timeInformation = timeInformation;
    }

    @Data
    public static class UserStat {
        private String  userId;         //아이디
        private String  idName;         //성명
        private String  groupName;      //부서명
        private String  groupCode;      //부서코드
        private String  groupTreeName;  //부서트리코드
        private Integer totalCnt;       //총 통화 전체 건수
        private Integer totalSuccess;   //총 통화 통화 건수
        private Integer totalBillSec;   //총 통화 시간

        private StatUserOutboundResponse outboundStat;
        private StatUserInboundResponse  inboundStat;
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
