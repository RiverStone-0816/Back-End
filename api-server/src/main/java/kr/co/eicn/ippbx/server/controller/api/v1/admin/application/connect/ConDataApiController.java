package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.connect;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.model.dto.eicn.ConGroupResponse;
import kr.co.eicn.ippbx.server.repository.eicn.ConGroupRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > 연동DB 관리 > 데이터관리
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/connect/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConDataApiController extends ApiBaseController {

    private final ConGroupRepository conGroupRepository;

    /**
     *  고객DB 그룹 목록
     */
    @GetMapping("con-group")
    public ResponseEntity<JsonResult<List<ConGroupResponse>>> conGroup() {
        return ResponseEntity.ok(data(conGroupRepository.findAll().stream()
                .map(e -> convertDto(e, ConGroupResponse.class))
                .sorted(comparing(ConGroupResponse::getSeq))
                .collect(Collectors.toList())));
    }
}
