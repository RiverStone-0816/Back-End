package kr.co.eicn.ippbx.server.controller.api.v1.admin.chat.config;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceSummaryInfoResponse;
import kr.co.eicn.ippbx.model.form.WebchatServiceInfoFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.WebChattingConfigService;
import kr.co.eicn.ippbx.util.JsonResult;
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
import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/chat/config/", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebchatConfigApiController extends ApiBaseController {
    private final WebChattingConfigService webChattingConfigService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<WebchatServiceSummaryInfoResponse>>> getAll() {
        List<WebchatServiceSummaryInfoResponse> response = webChattingConfigService.getAll();

        return ResponseEntity.ok(JsonResult.data(response));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<WebchatServiceInfoResponse>> get(@PathVariable Integer seq) {
        WebchatServiceInfoResponse response = webChattingConfigService.get(seq);

        return ResponseEntity.ok(JsonResult.data(response));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@RequestBody @Valid WebchatServiceInfoFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        webChattingConfigService.insert(form);

        return ResponseEntity.ok(JsonResult.create());
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> update(@PathVariable Integer seq, @RequestBody @Valid WebchatServiceInfoFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        webChattingConfigService.update(seq, form);

        return ResponseEntity.ok(JsonResult.create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        webChattingConfigService.delete(seq);

        return ResponseEntity.ok(JsonResult.create());
    }

    @GetMapping("image")
    public ResponseEntity<Resource> getImage(@RequestParam String fileName) {
        final Resource resource = webChattingConfigService.getImage(fileName);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.setCacheControl(CacheControl.noCache().getHeaderValue()))
                .body(resource);
    }

    @PostMapping("image")
    public ResponseEntity<JsonResult<String>> uploadImage(@RequestParam MultipartFile image) {
        final String saveFileName = webChattingConfigService.uploadImage(image);

        return ResponseEntity.ok(data(saveFileName));
    }
}
