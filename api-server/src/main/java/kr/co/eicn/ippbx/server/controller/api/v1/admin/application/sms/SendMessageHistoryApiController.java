package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.sms;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.SendMessageHistoryResponse;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > SMS 관리 > SMS 발송이력
 */

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/sms/message-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendMessageHistoryApiController extends ApiBaseController {

    private final SendMessageHistoryRepository repository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<SendMessageHistoryResponse>>> pagination(SendMessageSearchRequest search) {
        final Pagination<SendMessage> pagination = repository.pagination(search);
        final List<SendMessageHistoryResponse> rows = pagination.getRows().stream()
                .map(e -> convertDto(e, SendMessageHistoryResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }
}
