package kr.co.eicn.ippbx.model.dto.atcenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChattingResponse {
    private List<DetailResponse> data;
    private String resultCode;        // 결과코드
    private String resultMessage;     // 결과메시지

    @Data
    public static class DetailResponse {
        private String chnnlId;     // 채널id
        private String cnvrsSn;     // 대화순번
        private String cnvrsCont;   // 대화내용
        private String mbrNo;       // 대화참여자회원번호
        private String useYnCd;
        private String rgstnDtm;
        private String updusrIp;
    }
}
