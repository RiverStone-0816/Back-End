package kr.co.eicn.ippbx.front.controller.api.atcenter;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.ChattingApiInterface;
import kr.co.eicn.ippbx.front.service.api.atcenter.AtCenterService;
import kr.co.eicn.ippbx.model.form.atcenter.ChattFormRequest;
import kr.co.eicn.ippbx.model.search.atcenter.AtCenterSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@Slf4j
@LoginRequired
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/atcenter", produces = MediaType.APPLICATION_JSON_VALUE)
public class AtCenterApiController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AtCenterApiController.class);

    private final AtCenterService atCenterService;
    private final ChattingApiInterface chattingApiInterface;

    /**
     * 고객정보 및 업체정보 반환
     * @param searchRequest
     */
    @GetMapping("/members")
    public ResponseEntity<?> findMembers(AtCenterSearchRequest searchRequest) {
        try {
            return ResponseEntity.ok(atCenterService.getMembers(searchRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 온라인거래소 코드 조회
     * @param searchRequest
     */
    @GetMapping("/codes")
    public ResponseEntity<?> getCodes(AtCenterSearchRequest.AtCenterSearchCodeRequest searchRequest) {
        try {
            return ResponseEntity.ok(atCenterService.getCodes(searchRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 고객 거래 정보 조회
     * @param searchRequest
     */
    @GetMapping("/trades")
    public ResponseEntity<?> getTrades(AtCenterSearchRequest.AtCenterSearchTradeRequest searchRequest) {
        try {
            return ResponseEntity.ok(atCenterService.getTrades(searchRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 1:1 채팅방 목록 조회
     * @param searchChatRequest
     */
    @GetMapping("/chatRoom")
    public ResponseEntity<?> getChatRoom(AtCenterSearchRequest.AtCenterSearchChatRoomRequest searchChatRequest) {
        try {
            return ResponseEntity.ok(atCenterService.getChatRoom(searchChatRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 1:1 채팅 내용 조회
     * @param searchChatRequest
     */
    @GetMapping("/chats")
    public ResponseEntity<?> getChats(AtCenterSearchRequest.AtCenterSearchChatRequest searchChatRequest) {
        try {
            return ResponseEntity.ok(atCenterService.getChats(searchChatRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 1:1 채팅 저장
     * @param request
     */
    @PostMapping("/chats")
    public void updateChats(ChattFormRequest request) throws IOException, ResultFailException {
        // TODO:: 수정 필요
        atCenterService.updateChats(request);

    }
}
