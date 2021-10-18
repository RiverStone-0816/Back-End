package kr.co.eicn.ippbx.server.controller.api.v1.admin.chatbot.event;

import kr.co.eicn.ippbx.model.dto.customdb.ChatbotEventHistoryResponse;
import kr.co.eicn.ippbx.model.form.ChatbotSendEventFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotEventHistorySearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.ChatbotEventHistoryService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/chatbot/event/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatbotEventHistoryApiController extends ApiBaseController {
    private final Logger logger = LoggerFactory.getLogger(ChatbotEventHistoryApiController.class);

    private final ChatbotEventHistoryService chatbotEventHistoryService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<ChatbotEventHistoryResponse>>> getPagination(ChatbotEventHistorySearchRequest search) {
        return ResponseEntity.ok(JsonResult.data(chatbotEventHistoryService.getPagination(search)));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(ChatbotSendEventFormRequest form) {
        return ResponseEntity.ok(JsonResult.data(chatbotEventHistoryService.insert(form)));
    }
}
