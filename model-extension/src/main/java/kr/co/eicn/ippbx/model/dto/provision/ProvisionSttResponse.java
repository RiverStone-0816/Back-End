package kr.co.eicn.ippbx.model.dto.provision;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ProvisionSttResponse {
    @Schema(description = "상담 고유키")
    private String resultSeq;

    @Schema(description = "상담 내용 목록")
    private List<SttRow> sttList;

    @Schema(description = "상담원 상태")
    private String  status;
    @Schema(description = "발신 총시도콜")
    private Integer outTotal   = 0;
    @Schema(description = "발신 성공호")
    private Integer outSuccess = 0;
    @Schema(description = "발신 비수신")
    private Integer outCancel  = 0;
    @Schema(description = "수신 요청호")
    private Integer inTotal    = 0;
    @Schema(description = "수신 응대호")
    private Integer inSuccess  = 0;
    @Schema(description = "수신 개인비수신")
    private Integer inCancel   = 0;

    @Data
    public static class SttRow {
        @Schema(description = "상담시간")
        private Timestamp insertTime;
        @Schema(description = "상담내용")
        private String    message;
        @Schema(description = "상담원/고객 여부")
        private String    kind;
    }
}
