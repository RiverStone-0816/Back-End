package kr.co.eicn.ippbx.model.dto.provision;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProvisionStatUserResponse {

  @Schema(description = "상담원ID")
  private String userId;

  @Schema(description = "상담원명")
  private String idName;

  @Schema(description = "상담원상태명")
  private String statUser;

  @Schema(description = "입입콜수")
  private Integer inboundTotal;

  @Schema(description = "상담건수")
  private Integer totalCount;

  @Schema(description = "포기건수")
  private Integer inboundCancel;

  @Schema(description = "후처리건수")
  private Integer postProcess;

  @Schema(description = "휴식건수")
  private Integer rest;
}