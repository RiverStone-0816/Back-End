package kr.co.eicn.ippbx.server.controller.api.v1.admin.chatbot.history;

import kr.co.eicn.ippbx.model.dto.customdb.ChatbotHistoryResponse;
import kr.co.eicn.ippbx.model.search.ChatbotHistorySearchRequest;
import kr.co.eicn.ippbx.model.search.ChatbotProfileMsgSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.ChatbotHistoryService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/chatbot/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatbotHistoryApiController extends ApiBaseController {

    private final ChatbotHistoryService chatbotHistoryService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<ChatbotHistoryResponse>>> getPagination(ChatbotHistorySearchRequest search) {
        return ResponseEntity.ok(data(chatbotHistoryService.getPagination(search)));
    }

    @GetMapping("chat-message")
    public ResponseEntity<JsonResult<List<ChatbotHistoryResponse>>> getChatbotMessageHistory(ChatbotProfileMsgSearchRequest search) {
        return ResponseEntity.ok(data(chatbotHistoryService.getChatbotMessageHistory(search)));
    }
}
