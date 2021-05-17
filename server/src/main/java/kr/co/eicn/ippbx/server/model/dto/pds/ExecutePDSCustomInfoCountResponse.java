package kr.co.eicn.ippbx.server.model.dto.pds;

import lombok.Data;

@Data
public class ExecutePDSCustomInfoCountResponse {
    private Integer totalTry;   //진행된건
    private Integer totalMod;   //남은건
    private Integer rings;      //시도
    private Integer dialed;     //통화
    private Integer hanged;     //종료
    private Integer rst1;       //수신
    private Integer rst2;       //비수신
    private Integer rst3;       //통화중
    private Integer rst4;       //비연결
    private Integer rst5;       //기타
}
