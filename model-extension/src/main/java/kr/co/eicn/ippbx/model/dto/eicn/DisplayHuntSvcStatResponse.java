package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class DisplayHuntSvcStatResponse {
    private String svcName;     //대표서비스명
    private String svcNumber;     //대표서비스번호
    private Integer total;  //인입호
    private Integer onlyRead;   //단순조회
    private Integer connReq;    //연결요청
    private Integer success;    //응대호
    private Integer cancel;     //포기호
    private Double  resAve;     //응답률
}
