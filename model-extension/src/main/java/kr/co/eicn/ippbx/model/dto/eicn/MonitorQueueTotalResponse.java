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
        this.callbackProcessingRate = Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(callbackSuccess, calllback)));
    }

    @JsonIgnore
    public void setCallCounselRate(Integer loginUserCnt, Integer workingUserCnt) {
        this.callCounselRate = Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(loginUserCnt, workingUserCnt)));
    }

    @JsonIgnore
    public void setCounselorStatus(Integer waitUser, Integer workingUser) {
        this.counselorStatus = Double.parseDouble(String.format("%.2f", EicnUtils.getRateValue(waitUser, workingUser)));
    }
}
