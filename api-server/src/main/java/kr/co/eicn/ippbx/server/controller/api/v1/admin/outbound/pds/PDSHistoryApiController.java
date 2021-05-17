package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup;
import kr.co.eicn.ippbx.server.model.dto.eicn.PDSHistoryResponse;
import kr.co.eicn.ippbx.server.model.search.PDSHistorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.HistoryPDSGroupRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 *  아웃바운드관리 > PDS > 실행이력
 * */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSHistoryApiController extends ApiBaseController {
    private final HistoryPDSGroupRepository historyPDSGroupRepository;

    /**
     *  실행이력 조회
     * */
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PDSHistoryResponse>>> pagination(PDSHistorySearchRequest search) {
        Pagination<HistoryPdsGroup> pagination = historyPDSGroupRepository.pagination(search);
        List<PDSHistoryResponse> rows = pagination.getRows().stream()
                .map(historyPdsGroup -> convertDto(historyPdsGroup, PDSHistoryResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
    *   이력 삭제
    **/
    @DeleteMapping("execution/{executeId}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String executeId) {
        historyPDSGroupRepository.deleteByExecuteId(executeId);

        return ResponseEntity.ok(create());
    }
}
