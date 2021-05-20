package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.util.EicnUtils;
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
    public void setResponseRate(Integer success, Integer total) {
        this.responseRate = Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(success, total)));
    }

    @JsonIgnore
    public void setCallbackProcessingRate(Integer callbackSuccess, Integer calllback) {
        if (callbackSuccess == 0 || calllback == 0)
            this.callbackProcessingRate = 0d;
        this.callbackProcessingRate = callbackSuccess.doubleValue() / calllback;
    }

    @JsonIgnore
    public void setCallCounselRate(Integer loginUserCnt, Integer workingUserCnt) {
        if (loginUserCnt == 0 || workingUserCnt == 0)
            this.callCounselRate =  0d;
        this.callCounselRate = loginUserCnt.doubleValue() / workingUserCnt;
    }

    @JsonIgnore
    public void setCounselorStatus(Integer waitUser, Integer workingUser) {
        if (waitUser == 0 || workingUser == 0)
            this.counselorStatus = 0d;
        this.counselorStatus = waitUser.doubleValue() / workingUser;
    }
}
