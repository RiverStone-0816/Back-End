package kr.co.eicn.ippbx.server.controller.api.provision.stt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.eicn.ippbx.model.dto.provision.ProvisionSttResponse;
import kr.co.eicn.ippbx.model.search.provision.ProvisionSttSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.SttCdrService;
import kr.co.eicn.ippbx.server.service.SttMessageService;
import kr.co.eicn.ippbx.server.service.SttTextService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * [API 제공용] STT API
 */
@Tag(name = "STT API", description = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/provision/stt", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProvisionSttApiController extends ApiBaseController {

    private final SttCdrService     sttCdrService;
    private final SttTextService    sttTextService;
    private final SttMessageService sttMessageService;

    @Operation(summary = "고객 상담 STT 조회", description = "고객 상담내용 STT를 조회합니다.", security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("")
    public ResponseEntity<JsonResult<List<ProvisionSttResponse>>> list(@Parameter(description = "페이지 하단의 ProvisionSttSearchRequest를 참조해주세요.") ProvisionSttSearchRequest search) {
        final List<ProvisionSttResponse> list = new ArrayList<>();

        // 상담원 상태 정보 조회 로직


        // 반환
        return ResponseEntity.ok(data(list));
    }
}
