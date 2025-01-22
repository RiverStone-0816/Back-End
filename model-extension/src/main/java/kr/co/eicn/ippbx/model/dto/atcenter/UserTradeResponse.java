package kr.co.eicn.ippbx.model.dto.atcenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTradeResponse {
    public String cstmrTrnsInfo;      // 고객거래정보
    private String resultCode;        // 결과코드
    private String resultMessage;     // 결과메시지
}
