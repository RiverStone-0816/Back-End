package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

@Data
public class StatInboundResponse {
    private Integer total          = 0; //전체콜
    private Integer onlyRead       = 0; //단순조회
    private Integer connReq        = 0; //연결요청
    private Integer success        = 0; //응대호
    private Integer cancel         = 0; //포기호
    private Integer cancelNoAnswer = 0; //비수신 포기호
    private Integer cancelTimeout  = 0; //타임아웃 포기호
    private Integer cancelCustom   = 0; //고객 포기호
    private Integer callback       = 0; //콜백

    private Integer billSecSum               = 0;  //총 통화시간
    private Long    billSecAvg               = 0L; //평균 통화시간 = 총통화시간 / 응대호
    private Long    waitAvg                  = 0L; //평균 대기시간 = (총대기시간 - 총포기호대기시간) / 응대호
    private Double  responseRate             = 0d; //호응답률 = 응대호 / 연결요청
    private Double  callbackResponseRate     = 0d; //콜백포함 응답률 = (응대호 + 콜백) / 연결요청
    private Double  svcLevelAvg              = 0d; //서비스레벨 호응답률 = 서비스레벨을 만족한 응대호 / 연결요청
    private Double  quantitativeResponseRate = 0d; //양적개선 응답률 = (응대호 + 타임아웃 포기호 + 고객 포기호) / 연결요청
    private Double  qualitativeResponseRate  = 0d; //질적개선 응답률 = (응대호 + 비수신 포기호) / 연결요청
    private Double  ivrAvg                   = 0d; //단순조회율 = 단순조회 / 전체콜

    private Integer waitSucc_0_10  = 0; //응대호 ~10초
    private Integer waitSucc_10_20 = 0; //응대호 ~20초
    private Integer waitSucc_20_30 = 0; //응대호 ~30초
    private Integer waitSucc_30_40 = 0; //응대호 ~40초
    private Integer waitSucc_40    = 0; //응대호 ~40초이후

    private Integer waitCancel_0_10  = 0; //포기호 ~10초
    private Integer waitCancel_10_20 = 0; //포기호 ~20초
    private Integer waitCancel_20_30 = 0; //포기호 ~30초
    private Integer waitCancel_30_40 = 0; //포기호 ~40초
    private Integer waitCancel_40    = 0; //포기호 ~40초이후

    private Double cancelAvg = 0d; //호 포기율 = 포기호 / connReq
}
