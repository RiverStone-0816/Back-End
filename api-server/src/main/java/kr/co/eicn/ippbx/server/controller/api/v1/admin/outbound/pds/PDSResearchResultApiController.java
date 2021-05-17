package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup;
import kr.co.eicn.ippbx.server.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.server.model.search.HistoryPdsGroupSearchRequest;
import kr.co.eicn.ippbx.server.model.search.PDSResearchResultSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.HistoryPDSGroupRepository;
import kr.co.eicn.ippbx.server.repository.pds.PDSResearchResultRepository;
import kr.co.eicn.ippbx.server.service.PDSResearchResultService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 아웃바운드관리 > PDS > PDS결과이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/research-result-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSResearchResultApiController extends ApiBaseController {

    private final PDSResearchResultService service;
    private final HistoryPDSGroupRepository historyPDSGroupRepository;

    /**
     * 업로드필드정보, 캠페인결과?, 필드정보 list, 발신시간, 통화결과, 통화시간, 설문명
     */
    @GetMapping("{executeId}/data")
    public JsonResult<List<PdsResearchResultEntity>> getList(@PathVariable String executeId, PDSResearchResultSearchRequest search) {
        final PDSResearchResultRepository repository = service.getRepository(executeId);

        if (!repository.existsTable())
            return data(new ArrayList<>());

        return data(repository.findAll(search));
    }

    /**
     * 오토콜 실행목록 조회
     */
    @GetMapping("add-execute")
    public ResponseEntity<JsonResult<List<HistoryPdsGroup>>> addExecuteLists(HistoryPdsGroupSearchRequest search) {
        search.setConnectKinds(Arrays.asList("ARS_RSCH", "RSCH"));
        return ResponseEntity.ok(data(historyPDSGroupRepository.findAll(search)));
    }
}
