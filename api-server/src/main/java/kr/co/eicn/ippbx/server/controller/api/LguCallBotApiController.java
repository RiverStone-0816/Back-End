package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.model.dto.eicn.LguCallBotInfoResponse;
import kr.co.eicn.ippbx.server.repository.eicn.LguCallbotInfoRepository;
import kr.co.eicn.ippbx.util.JsonResult;
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
@RequestMapping(value = "api/call-bot", produces = MediaType.APPLICATION_JSON_VALUE)
public class LguCallBotApiController extends ApiBaseController {
    private final LguCallbotInfoRepository lguCallbotInfoRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<LguCallBotInfoResponse>>> get() {
        return ResponseEntity.ok(data(lguCallbotInfoRepository.getCallBotApiUrl()));
    }
}
