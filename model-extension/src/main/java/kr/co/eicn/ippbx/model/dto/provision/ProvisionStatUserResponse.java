package kr.co.eicn.ippbx.model.dto.provision;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProvisionStatUserResponse {
    @Schema(description = "상담원 아이디")
    private String id;
    @Schema(description = "상담원 상태명")
    private String status;

    @Schema(description = "수신 요청호. 인입콜수")
    private Integer inTotal   = 0;
    @Schema(description = "수신 응대호. 상담건수")
    private Integer inSuccess = 0;
    @Schema(description = "수신 개인비수신. 포기건수")
    private Integer inCancel  = 0;

    @Schema(description = "발신 총시도콜")
    private Integer outTotal   = 0;
    @Schema(description = "발신 성공호")
    private Integer outSuccess = 0;
    @Schema(description = "발신 비수신")
    private Integer outCancel  = 0;

    @Schema(description = "후처리 건수")
    private Integer post = 0;
    @Schema(description = "휴식 건수")
    private Integer rest = 0;
}
