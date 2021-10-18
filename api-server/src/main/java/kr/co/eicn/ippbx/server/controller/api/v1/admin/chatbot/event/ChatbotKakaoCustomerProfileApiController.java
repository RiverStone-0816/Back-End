package kr.co.eicn.ippbx.server.controller.api.v1.admin.chatbot.event;

import kr.co.eicn.ippbx.model.dto.customdb.ChatKaKaoProfileInfoResponse;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotKakaoCustomerProfileResponse;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotSendEventDataResponse;
import kr.co.eicn.ippbx.model.search.ChatbotKakaoCustomerProfileSearchRequest;
import kr.co.eicn.ippbx.model.search.ChatbotProfileMsgSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.ChatbotKakakoCustomerProfileService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/chatbot/event/profile", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatbotKakaoCustomerProfileApiController extends ApiBaseController {
    private final ChatbotKakakoCustomerProfileService chatbotKakakoCustomerProfileService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<ChatbotKakaoCustomerProfileResponse>>> getPagination(ChatbotKakaoCustomerProfileSearchRequest search) {
        return ResponseEntity.ok(JsonResult.data(chatbotKakakoCustomerProfileService.getPagination(search)));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<ChatbotSendEventDataResponse>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok(JsonResult.data(chatbotKakakoCustomerProfileService.get(seq)));
    }

    @GetMapping("profile-info")
    public ResponseEntity<JsonResult<ChatKaKaoProfileInfoResponse>> getProfileInfo(ChatbotProfileMsgSearchRequest search) {
        return ResponseEntity.ok(JsonResult.data(chatbotKakakoCustomerProfileService.getRepository().getProfileInfo(search)));
    }
}
