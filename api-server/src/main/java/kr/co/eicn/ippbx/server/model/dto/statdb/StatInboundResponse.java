package kr.co.eicn.ippbx.server.model.dto.statdb;

import lombok.Data;

@Data
public class StatInboundResponse {
    private Integer total = 0;  //전체콜
    private Integer onlyRead = 0;   //단순조회
    private Integer connReq = 0;    //연결요청
    private Integer success = 0;    //응대호
    private Integer cancel = 0; //포기호
    private Integer callbackSuccess = 0;

    private Integer billSecSum = 0; //총통화시간
    private Long billSecAvg = 0L; //평균 통화시간
    private Long waitAvg = 0L; //평균 대기시간
    private Double responseRate = 0d;     //호응답률
    private Double svcLevelAvg = 0d;    //서비스레벨호 응답률
    private Double ivrAvg = 0d;     //단순조회율

    private Integer waitSucc_0_10 = 0;  //응대호 ~10초
    private Integer waitSucc_10_20 = 0;  //응대호 ~20초
    private Integer waitSucc_20_30 = 0;  //응대호 ~30초
    private Integer waitSucc_30_40 = 0;  //응대호 ~40초
    private Integer waitSucc_40 = 0;  //응대호 ~40초이후

    private Integer waitCancel_0_10 = 0;   //포기호 ~10초
    private Integer waitCancel_10_20 = 0;   //포기호 ~20초
    private Integer waitCancel_20_30 = 0;   //포기호 ~30초
    private Integer waitCancel_30_40 = 0;   //포기호 ~40초
    private Integer waitCancel_40 = 0;   //포기호 ~40초이후
}
