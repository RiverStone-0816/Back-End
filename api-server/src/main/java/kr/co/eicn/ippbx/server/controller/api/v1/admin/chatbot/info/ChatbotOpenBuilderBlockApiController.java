package kr.co.eicn.ippbx.server.controller.api.v1.admin.chatbot.info;

import kr.co.eicn.ippbx.model.dto.customdb.ChatbotOpenBuilderInfoResponse;
import kr.co.eicn.ippbx.model.form.ChatbotOpenBuilderBlockFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotOpenBuilderBlockSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.ChatbotOpenBuilderBlockService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/chatbot/info/block", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatbotOpenBuilderBlockApiController extends ApiBaseController {
    private final ChatbotOpenBuilderBlockService chatbotOpenBuilderBlockService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<ChatbotOpenBuilderInfoResponse>>> getPagination(ChatbotOpenBuilderBlockSearchRequest search) {
        return ResponseEntity.ok(JsonResult.data(chatbotOpenBuilderBlockService.getPagination(search)));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<ChatbotOpenBuilderInfoResponse>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok(JsonResult.data(chatbotOpenBuilderBlockService.get(seq)));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@RequestBody ChatbotOpenBuilderBlockFormRequest form) {
        chatbotOpenBuilderBlockService.post(form);

        return ResponseEntity.ok(JsonResult.data(null));
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> put(@PathVariable Integer seq, @RequestBody ChatbotOpenBuilderBlockFormRequest form) {
        chatbotOpenBuilderBlockService.put(seq, form);

        return ResponseEntity.ok(JsonResult.data(null));
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        chatbotOpenBuilderBlockService.delete(seq);

        return ResponseEntity.ok(JsonResult.data(null));
    }
}
