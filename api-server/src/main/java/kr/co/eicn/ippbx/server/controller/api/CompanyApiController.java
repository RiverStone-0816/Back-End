package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryCompanyServerResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyLicenceEntity;
import kr.co.eicn.ippbx.model.enums.ServerType;
import kr.co.eicn.ippbx.server.repository.eicn.CmpMemberStatusCodeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyServerRepository;
import kr.co.eicn.ippbx.server.service.CompanyService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 공통 고객사 API 인터페이스
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/company", produces = MediaType.APPLICATION_JSON_VALUE)
public class CompanyApiController extends ApiBaseController {

    private final CompanyService companyService;
    private final CompanyServerRepository companyServerRepository;
    private final CmpMemberStatusCodeRepository cmpMemberStatusCodeRepository;

    /**
     * 고객사 라이센스 정보를 반환
     */
    @GetMapping("licence")
    public ResponseEntity<JsonResult<CompanyLicenceEntity>> getLicense() {
        return ResponseEntity.ok(data(companyService.getCompanyLicenceInfo()));
    }

    @GetMapping("info")
    public ResponseEntity<JsonResult<CompanyEntity>> getInfo() {
        return ResponseEntity.ok(data(g.getUser().getCompany()));
    }

    @GetMapping("info/{companyId}")
    public ResponseEntity<JsonResult<CompanyInfo>> getInfo(@PathVariable String companyId) {
        final CompanyInfo companyInfo = companyService.getCompanyInfo(companyId);
        return ResponseEntity.ok(data(companyInfo));
    }

    @GetMapping("member-status-codes")
    public JsonResult<List<CmpMemberStatusCodeEntity>> getMemberStatusCodes() {
        return JsonResult.data(cmpMemberStatusCodeRepository.findAll());
    }

    /**
     * 실행할교환기 목록 조회
     */
    @GetMapping("add-server")
    public ResponseEntity<JsonResult<List<SummaryCompanyServerResponse>>> addServerLists() {
        return ResponseEntity.ok(data(companyServerRepository.findAllType(ServerType.PBX)
                .stream()
                .map(e -> {
                    SummaryCompanyServerResponse response = convertDto(e, SummaryCompanyServerResponse.class);
                    response.setName(e.getServer().getName());
                    return response;
                })
                .collect(Collectors.toList()))
        );
    }

    @GetMapping("pbx-server")
    public ResponseEntity<JsonResult<ServerInfo>> pbxServerInfo() {
        ServerInfo serverInfo = companyServerRepository.findCompanyInfo();
        return ResponseEntity.ok(data(serverInfo));
    }

    @GetMapping("check-service")
    public ResponseEntity<JsonResult<Boolean>> checkService(@RequestParam String service) {
        return ResponseEntity.ok(data(companyService.checkService(g.getUser().getCompanyId(), service)));
    }

    @GetMapping("get-service")
    public ResponseEntity<JsonResult<String>> checkService() {
        return ResponseEntity.ok(data(companyService.getServices(g.getUser().getCompanyId())));
    }

}
