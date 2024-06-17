package kr.co.eicn.ippbx.server.controller.api.v1.admin.chat.bot;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotBlockSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotFallbackInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatBotSummaryInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatBotFallbackFormRequest;
import kr.co.eicn.ippbx.model.form.WebchatBotFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.WebchatBotBlockService;
import kr.co.eicn.ippbx.server.service.WebchatBotInfoService;
import kr.co.eicn.ippbx.server.service.WebchatBotService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/chat/bot/", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebchatBotInfoApiController extends ApiBaseController {
    private final WebchatBotInfoService webchatBotInfoService;
    private final WebchatBotService webchatBotService;
    private final WebchatBotBlockService webchatBotBlockService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<WebchatBotSummaryInfoResponse>>> list() {
        return ResponseEntity.ok(data(webchatBotInfoService.getAllWebchatBotList()));
    }

    @GetMapping("pagination")
    public ResponseEntity<JsonResult<Pagination<WebchatBotSummaryInfoResponse>>> pagination(ChatbotSearchRequest request) {
        return ResponseEntity.ok(data(webchatBotInfoService.pagination(request)));
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonResult<WebchatBotInfoResponse>> getBotInfo(@PathVariable Integer id) {
        WebchatBotInfoResponse response = webchatBotService.getBotInfo(id);

        return ResponseEntity.ok(data(response));
    }

    @GetMapping("{id}/fallback")
    public ResponseEntity<JsonResult<WebchatBotFallbackInfoResponse>> getBotFallbackInfo(@PathVariable Integer id) {
        WebchatBotFallbackInfoResponse response = webchatBotInfoService.getFallbackInfo(id);

        return ResponseEntity.ok(data(response));
    }

    @GetMapping("blocks/{blockId}")
    public ResponseEntity<JsonResult<WebchatBotInfoResponse.BlockInfo>> getBlock(@PathVariable Integer blockId) {
        WebchatBotInfoResponse.BlockInfo blockInfo = webchatBotService.getBlockInfo(blockId);

        return ResponseEntity.ok(data(blockInfo));
    }

    @GetMapping("blocks/template")
    public ResponseEntity<JsonResult<List<WebchatBotBlockSummaryResponse>>> getTemplateBlockList() {
        List<WebchatBotBlockSummaryResponse> templateBlockList = webchatBotBlockService.getTemplateBlockList();

        return ResponseEntity.ok(data(templateBlockList));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody WebchatBotFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

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
    public ResponseEntity<JsonResult<String>> uploadImage(@RequestParam MultipartFile image) throws IOException {
        final String saveFileName = webchatBotService.uploadImage(image);

        return ResponseEntity.ok(data(saveFileName));
    }

    @PutMapping("{id}")
    public ResponseEntity<JsonResult<Void>> updateBotInfo(@PathVariable Integer id, @Valid @RequestBody WebchatBotFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        try {
            webchatBotService.updateWebchatBotInfo(id, form, false);
        } catch (Exception e) {
            throw new RuntimeException("수정중 오류가 발생하여 이전 데이터로 복구합니다.");
        }

        return ResponseEntity.ok(create());
    }

    @PutMapping("{id}/all")
    public ResponseEntity<JsonResult<Void>> update(@PathVariable Integer id, @Valid @RequestBody WebchatBotFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        try {
            webchatBotService.updateWebchatBotInfo(id, form, true);
        } catch (Exception e) {
            log.error("Exception!", e);
            throw new RuntimeException("수정중 오류가 발생하여 이전 데이터로 복구합니다.");
        }

        return ResponseEntity.ok(create());
    }

    @PutMapping("{id}/fallback")
    public ResponseEntity<JsonResult<Void>> fallbackUpdate(@PathVariable Integer id, @Valid @RequestBody WebchatBotFallbackFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        try {
            webchatBotInfoService.updateFallbackInfo(id, form);
        } catch (Exception e) {
            throw new RuntimeException("수정중 오류가 발생하여 이전 데이터로 복구합니다.");
        }

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
