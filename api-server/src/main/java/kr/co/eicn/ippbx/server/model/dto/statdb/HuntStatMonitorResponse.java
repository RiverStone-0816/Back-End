package kr.co.eicn.ippbx.server.model.dto.statdb;

import kr.co.eicn.ippbx.server.util.EicnUtils;
import lombok.Data;

@Data
public class HuntStatMonitorResponse {
    private String huntName;        //큐명
    private Integer total = 0;        //인입호
    private Integer connectionRequest = 0;   //연결요청
    private Integer success = 0;      //응답호
    private Integer cancel = 0;    //포기호
    private Integer callback = 0;       //콜백
    private Float responseRate = 0f;        //응답률

    public void setResponseRate() {
        responseRate = EicnUtils.getRateValue(success, total);
    }
}
