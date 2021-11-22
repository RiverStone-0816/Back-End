package kr.co.eicn.ippbx.server.controller.api.v1.chatbot.info;

import kr.co.eicn.ippbx.model.dto.eicn.SummaryWebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.WebchatBotInfoService;
import kr.co.eicn.ippbx.server.service.WebchatBotService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/chat-bot/info/", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebchatBotInfoApiController extends ApiBaseController {
    private final WebchatBotInfoService webchatBotInfoService;
    private final WebchatBotService webchatBotService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<SummaryWebchatBotInfoResponse>>> list() {
        return ResponseEntity.ok(data(webchatBotInfoService.getAllWebchatBotList()));
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonResult<WebchatBotInfoResponse>> getById(@PathVariable Integer id) {
        WebchatBotInfoResponse webchatBotInfoResponse = webchatBotService.get(id);
        return ResponseEntity.ok(data(webchatBotInfoResponse));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(@RequestBody WebchatBotFormRequest form) {
        final Integer botId = webchatBotService.createWebchatBotInfo(form);

        return ResponseEntity.ok(data(botId));
    }

    @GetMapping("image")
    public ResponseEntity<Resource> getImage(@RequestParam String fileName) {
        final Resource resource = webchatBotService.getImage(fileName);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.setCacheControl(CacheControl.noCache().getHeaderValue()))
                .body(resource);
    }

    @PostMapping("image")
    public ResponseEntity<JsonResult<String>> uploadImage(@RequestParam MultipartFile image) {
        final String saveFileName = webchatBotService.uploadImage(image);

        return ResponseEntity.ok(data(saveFileName));
    }

    @PutMapping("{id}")
    public ResponseEntity<JsonResult<Void>> update(@PathVariable Integer id, @RequestBody WebchatBotFormRequest form) {
        webchatBotService.updateWebchatBotInfo(id, form);

        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer id) {
        webchatBotService.deleteBot(id);

        return ResponseEntity.ok(create());
    }

    @PostMapping("{id}/copy")
    public ResponseEntity<JsonResult<Integer>> copy(@PathVariable Integer id) {
        Integer botId = webchatBotService.copy(id);

        return ResponseEntity.ok(data(botId));
    }
}
