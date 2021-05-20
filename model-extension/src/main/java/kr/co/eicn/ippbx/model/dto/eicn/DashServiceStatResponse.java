package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.Comparator;
import java.util.TreeMap;

@Data
public class DashServiceStatResponse {
    private String svcName;
    private Integer totalCnt;  //인입호수
    private Integer connReqCnt;  //연결요청건수
    private Integer successCnt; //응대호수
    private float rateValue; ///응대율
    private Integer serviceLevelCnt; //서비스레벨건수
    private float serviceLevelRateValue; ///서비스레벨응대율

    private TreeMap<Integer, Float> hourToResponseRate = new TreeMap<>(Comparator.comparingInt(e -> e));
}
