package kr.co.eicn.ippbx.server.controller.api.provision.result;

import static kr.co.eicn.ippbx.util.JsonResult.data;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import kr.co.eicn.ippbx.model.form.provision.ProvisionResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.customdb.ProvisionResultRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [API 제공용] 상담 API
 */
@Tag(name = "상담 API", description = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/provision/result", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProvisionResultApiController extends ApiBaseController {

  private final ProvisionResultRepository provisionResultRepository;

  @Operation(summary = "Q&A (출장) 상담 등록", description = "온라인 거래소에서 등록한 Q&A(출장) 상담내용을 상담관리 시스템(IPCC)에 전송합니다.", security = {
      @SecurityRequirement(name = "bearer-key")})
  @PostMapping("")
  public ResponseEntity<JsonResult<Boolean>> RegisterResult(
      @Parameter(description = "페이지 하단의 ProvisionResultCustomInfoFormRequest를 참조해주세요.") @Valid @RequestBody ProvisionResultCustomInfoFormRequest request) {
    provisionResultRepository.saveConsultation(request);
    return ResponseEntity.ok(data(true));
  }
}
