package kr.co.eicn.ippbx.model.form.atcenter;

import lombok.Data;;

@Data
public class CustomResultFormRequest {
    private String firmNo;              // 업체번호
    private String firmNm;              // 회사명
    private String brno;                // 사업자번호
    private String rprsTelNo;           // 대표전화번호
    private String firmEmailAddr;       // 업체이메일
    private String mmno;                // 회원번호
    private String mbrNm;               // 회원명
    private String moblTelNo;           // 휴대폰번호
    private String rcptsMbrNo;          // 접수자회원번호
    private String lastPrcrMbrNo;       // 최종처리자회원번호
    private String innerChargerMbrNo;   // 내부담당자회원번호

    private String cnsltTtl;            // 상담제목
    private String cnsltRequstCont;     // 상담요청내용
    private String cnsltAnsCont;        // 상담답변내용
    private String cnsltTypeLclsCd;     // 상담유형대분류코드
    private String cnsltTypeMclsCd;     // 상담유형중분류코드
    private String cnsltAcptChnnlCd;    // 상담접수채널코드
    private String cnsltId;             // 상담id

    private String chgYn;               // 변경여부코드
}
