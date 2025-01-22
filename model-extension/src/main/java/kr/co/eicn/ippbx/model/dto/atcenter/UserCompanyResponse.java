package kr.co.eicn.ippbx.model.dto.atcenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCompanyResponse {
    private List<DetailResponse> data;
    private String resultCode;        // 결과코드
    private String resultMessage;     // 결과메시지

    @Data
    public static class DetailResponse {
        private String firmNo;            // 업체번호
        private String firmNm;            // 회사명
        private String brno;              // 사업자번호
        private String jurirno;           // 법인등록번호
        private String addr;              // 주소
        private String firmEmailAddr;     // 업체이메일
        private String rprsTelno;         // 대표전화번호
        private String firmTypeCd;        // 업체타입
        private String firmTypeNm;        // 업체타입명
        private String psitnTypeCd;       // 소속구분코드
        private String psitnTypeNm;       // 소속구분코드명
        private String trmtClsCd;         // 취급분류코드
        private String trmtClsNm;         // 취급분류코드명
        private String rgstnStatCd;       // 등록상태코드
        private String rgstnStatNm;       // 등록상태명
        private String firmStatCd;        // 업체상태코드명
        private String firmStatNm;        // 업체상태코드명
        private String mmno;              // 회원번호
        private String mbrNm;             // 회원명
        private String emailAddr;         // 회원이메일
        private String moblTelno;         // 휴대폰번호
        private String mbrYn;             // 회원가입여부
    }
}
