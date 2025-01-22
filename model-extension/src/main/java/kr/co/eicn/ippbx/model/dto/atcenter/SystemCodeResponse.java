package kr.co.eicn.ippbx.model.dto.atcenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemCodeResponse {
    private List<DetailResponse> data;
    private String resultCode;        // 결과코드
    private String resultMessage;     // 결과메시지

    @Data
    public static class DetailResponse {
        private String comCodeList;
        private String urnkCd;            // 코드id
        private String urnkCdNm;          // 코드명
        private String urnkCdDscrp;
        private String lwprtCd;           // 상세코드id
        private String lwprtCdNm;         // 상세코드명
        private String afdOneColNm;       // 예비컬럼1
        private String afdTwoColNm;       // 예비컬럼2
        private String afdThreeColNm;     // 예비컬럼3
        private String afdFourColNm;      // 예비컬럼4
        private String afdFiveColNm;      // 예비컬럼5
        private String jobDvCd;
        private String value;
        private String cdValue;
        private String text;
    }
}
