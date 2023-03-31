package kr.co.eicn.ippbx.server.controller.api.v1.admin.application.sms;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMessageData;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.model.dto.eicn.SendMessageHistoryResponse;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.MessageDataService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 상담어플리케이션 관리 > SMS 관리 > SMS 발송이력
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/application/sms/message-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class SendMessageHistoryApiController extends ApiBaseController {

    private final SendMessageHistoryRepository repository;
    private final MessageDataService messageDataService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<SendMessageHistoryResponse>>> pagination(SendMessageSearchRequest search) {
        final Pagination<CommonMessageData> pagination = messageDataService.getRepository().pagination(search);
        final List<SendMessageHistoryResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    final SendMessageHistoryResponse response = new SendMessageHistoryResponse();
                    response.setId(e.getSeq());
                    response.setSendDate(e.getSendTime());
                    response.setTarget(e.getPhoneNumber());
                    response.setResMessage(e.getResMessage());
                    response.setContent(e.getMessage());
                    response.setSendType(e.getService());

                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }


}
