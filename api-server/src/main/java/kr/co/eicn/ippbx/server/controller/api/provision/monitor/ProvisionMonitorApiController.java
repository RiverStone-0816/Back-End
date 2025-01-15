package kr.co.eicn.ippbx.server.controller.api.provision.monitor;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.co.eicn.ippbx.model.dto.provision.ProvisionStatUserResponse;
import kr.co.eicn.ippbx.server.service.provision.monitor.ProvisionMonitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [API 제공용] 모니터링 API
 */
@Tag(name = "모니터링 API", description = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/provision/monitor", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProvisionMonitorApiController {

  private final ProvisionMonitorService provisionMonitorService;

  /**
   * 상담원 상태 정보를 조회
   */
  @GetMapping("")
  public ResponseEntity<List<ProvisionStatUserResponse>> getAgentCallStatus() {
    var result = provisionMonitorService.getAgentCallStatus();
    return ResponseEntity.ok(result);
  }
}
