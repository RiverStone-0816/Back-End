package kr.co.eicn.ippbx.server.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.server.util.EicnUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MonitorQueueTotalResponse {
    private Double responseRate = 0d;
    private Double callbackProcessingRate = 0d;  //콜백 처리율
    private Double callCounselRate = 0d; //상담 가용률
    private Double counselorStatus = 0d; //상담원 상태

    private List<MonitorQueueStatResponse> statList = new ArrayList<>();

    @JsonIgnore
    public Double getTResponseRate(Integer success, Integer total) {
        return Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(success, total)));
    }

    @JsonIgnore
    public Double getCallbackRate(Integer callbackSuccess, Integer calllback) {
        if (callbackSuccess == 0 || calllback == 0)
            return 0d;
        return callbackSuccess.doubleValue() / calllback;
    }

    @JsonIgnore
    public Double getCallAvailabilityRate(Integer loginUserCnt, Integer workingUserCnt) {
        if (loginUserCnt == 0 || workingUserCnt == 0)
            return 0d;
        return loginUserCnt.doubleValue() / workingUserCnt;
    }

    @JsonIgnore
    public Double getCounselorStatus(Integer waitUser, Integer workingUser) {
        if (waitUser == 0 || workingUser == 0)
            return 0d;
        return waitUser.doubleValue() / workingUser;
    }
}
