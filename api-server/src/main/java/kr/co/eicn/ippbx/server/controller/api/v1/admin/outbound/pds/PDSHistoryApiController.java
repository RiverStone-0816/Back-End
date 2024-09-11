package kr.co.eicn.ippbx.server.controller.api.v1.admin.outbound.pds;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.HistoryPdsGroup;
import kr.co.eicn.ippbx.model.dto.eicn.PDSHistoryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.search.PDSHistorySearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.ExecutePDSGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.HistoryPDSGroupRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 아웃바운드관리 > PDS > 실행이력
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/outbound/pds/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSHistoryApiController extends ApiBaseController {

    private final HistoryPDSGroupRepository historyPDSGroupRepository;
    private final ExecutePDSGroupRepository executePDSGroupRepository;

    /**
     * 실행이력 조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PDSHistoryResponse>>> pagination(PDSHistorySearchRequest search) {
        final Pagination<HistoryPdsGroup> pagination = historyPDSGroupRepository.pagination(search);
        final List<PDSHistoryResponse> rows = pagination.getRows().stream()
                .map(historyPdsGroup -> convertDto(historyPdsGroup, PDSHistoryResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 이력 삭제
     **/
    @DeleteMapping("execution/{executeId}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String executeId) {
        final List<HistoryPdsGroup> list = historyPDSGroupRepository.findAllByExecuteId(executeId);

        if (CollectionUtils.isEmpty(list))
            throw new IllegalStateException("해당 실행이력이 존재하지 않습니다.");

        final ExecutePDSGroupEntity entity = executePDSGroupRepository.findByRunHost(list.get(0).getRunHost(), executeId);

        if (Objects.nonNull(entity))
            throw new IllegalStateException("모니터링 중으로 삭제할 수 없습니다.");

        historyPDSGroupRepository.deleteByExecuteId(executeId);

        return ResponseEntity.ok(create());
    }
}
