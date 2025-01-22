package kr.co.eicn.ippbx.model.dto.atcenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoomResponse {
    private List<DetailResponse> data;
    private String resultCode;        // 결과코드
    private String resultMessage;     // 결과메시지

    @Data
    public static class DetailResponse {
        private String chnnlId;     // 채널id
        private String chnnlNm;     // 채널명
    }
}
