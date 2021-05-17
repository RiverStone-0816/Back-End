package kr.co.eicn.ippbx.server.controller.api.v1.search;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PdsGroup;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchPDSGroupResponse;
import kr.co.eicn.ippbx.server.repository.eicn.PDSGroupRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 *  PDS그룹 목록 조회
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/search/pds-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PDSGroupSearchApiController extends ApiBaseController {
    private final PDSGroupRepository pdsGroupRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<SearchPDSGroupResponse>>> list() {
        return ResponseEntity.ok(data(
                pdsGroupRepository.findAllOrderByLastExecuteDate().stream()
                        .map(pdsGroup -> convertDto(pdsGroup, SearchPDSGroupResponse.class))
                        .collect(Collectors.toList())
        ));
    }
}
