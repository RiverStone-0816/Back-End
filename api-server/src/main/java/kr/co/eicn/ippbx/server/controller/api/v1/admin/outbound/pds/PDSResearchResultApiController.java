package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.model.dto.eicn.HistoryPdsResearchGroupResponse;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.model.search.PDSResearchResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.HistoryPDSGroupRepository;
import kr.co.eicn.ippbx.server.service.PDSResearchResultService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드관리 > PDS > PDS결과이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/research-result-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSResearchResultApiController extends ApiBaseController {

    private final PDSResearchResultService  service;
    private final HistoryPDSGroupRepository historyPDSGroupRepository;

    @GetMapping("{executeId}/pagination")
    public ResponseEntity<JsonResult<Pagination<PdsResearchResultEntity>>> pagination(@PathVariable String executeId, PDSResearchResultSearchRequest search) {
        search.setExecuteId(executeId);
        return ResponseEntity.ok(data(service.getRepository(executeId).pagination(search)));
    }

    @GetMapping("{executeId}/list")
    public ResponseEntity<JsonResult<List<PdsResearchResultEntity>>> list(@PathVariable String executeId, PDSResearchResultSearchRequest search) {
        search.setExecuteId(executeId);
        return ResponseEntity.ok(data(service.getRepository(executeId).findAll(search)));
    }

    /**
     * 설문 실행 목록 조회
     */
    @GetMapping("execute-pds-info")
    public ResponseEntity<JsonResult<List<HistoryPdsResearchGroupResponse>>> getExecutingPdsList() {
        return ResponseEntity.ok(data(historyPDSGroupRepository.getResearchExecuteList()));
    }
}
