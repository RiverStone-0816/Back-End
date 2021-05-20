package kr.co.eicn.ippbx.model.dto.statdb;

import kr.co.eicn.ippbx.util.EicnUtils;
import lombok.Data;

@Data
public class TotalStatResponse {
    private Float rateValue = 0f;           //응대율
    private Integer serviceTotal;           //대표서비스총합
    private Integer directTotal;            //직통총합
    private Integer viewCount = 0;          //단순조회
    private Integer serviceConnectionRequest;   //대표서비스 연결요청
    private Integer directConnectionRequest;    //직통 연결요청
    private Integer serviceSuccess = 0;     //대표서비스 IB응대호
    private Integer directSuccess = 0;     //직통 IB응대호
    private Integer serviceCancel = 0;     //대표서비스 IB포기호
    private Integer directCancel = 0;     //직통 IB포기호
    private Integer callbackSuccess = 0;           //콜백
    private Integer outboundSuccess = 0;    //OB응대호
    private Integer outboundCancel = 0;     //OB무응답호

    public void setRateValue() {
        rateValue = EicnUtils.getRateValue(serviceSuccess + directSuccess, serviceConnectionRequest + directTotal);
    }
}
