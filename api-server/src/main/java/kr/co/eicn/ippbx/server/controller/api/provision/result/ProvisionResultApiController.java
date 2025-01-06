package kr.co.eicn.ippbx.server.controller.api.provision.result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.eicn.ippbx.model.form.provision.ProvisionResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.MaindbGroupRepository;
import kr.co.eicn.ippbx.server.service.MaindbCustomInfoService;
import kr.co.eicn.ippbx.server.service.ResultCustomInfoService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * [API 제공용] 상담 API
 */
@Tag(name = "상담 API", description = "")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/provision/result", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProvisionResultApiController extends ApiBaseController {

    private final MaindbGroupRepository   maindbGroupRepository;
    private final MaindbCustomInfoService maindbCustomInfoService;
    private final ResultCustomInfoService resultCustomInfoService;

    @Operation(summary = "Q&A (출장) 상담 등록", description = "온라인 거래소에서 등록한 Q&A(출장) 상담내용을 상담관리 시스템(IPCC)에 전송합니다.", security = {@SecurityRequirement(name = "bearer-key")})
    @PostMapping("")
    public ResponseEntity<JsonResult<Boolean>> RegisterResult(@Parameter(description = "페이지 하단의 ProvisionResultCustomInfoFormRequest를 참조해주세요.") @Valid @RequestBody ProvisionResultCustomInfoFormRequest from) {
        // 1. 고객키 조회

        // 2. 찾은 고객키 값으로 상담 등록

        // 3. 성공 여부 반환

        return ResponseEntity.ok(data(true));
    }
}
