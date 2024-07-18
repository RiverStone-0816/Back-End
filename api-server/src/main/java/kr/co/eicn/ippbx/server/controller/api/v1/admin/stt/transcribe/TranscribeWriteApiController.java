package kr.co.eicn.ippbx.server.controller.api.v1.admin.stt.transcribe;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeWriteResponse;
import kr.co.eicn.ippbx.model.form.TranscribeWriteFormRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.TranscribeDataService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stt/transcribe/write", produces = MediaType.APPLICATION_JSON_VALUE)
public class TranscribeWriteApiController extends ApiBaseController {
    private final TranscribeDataService transcribeDataService;

    @GetMapping("{fileSeq}")
    public ResponseEntity<JsonResult<TranscribeWriteResponse>> get(@PathVariable Integer fileSeq) throws JsonProcessingException {
        return ResponseEntity.ok(JsonResult.data(transcribeDataService.getWriteInfo(fileSeq)));
    }

    @PutMapping("{fileSeq}")
    public ResponseEntity<JsonResult<String>> update(@PathVariable Integer fileSeq, @RequestBody TranscribeWriteFormRequest request) throws JsonProcessingException {
        try {
            transcribeDataService.update(fileSeq, request);
        } catch (Exception e) {
            return ResponseEntity.ok(JsonResult.data("NOK"));
        }

        return ResponseEntity.ok(JsonResult.data("OK"));
    }

    @GetMapping(value = "{fileSeq}/resource")
    public ResponseEntity<Resource> resource(@PathVariable Integer fileSeq) {
        Resource resource = transcribeDataService.getResources(fileSeq);

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> {
                    header.add(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.builder("attachment")
                                    .filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8)
                                    .build().toString());
                    header.setPragma("no-cache");
                    header.setCacheControl(CacheControl.noCache());
                })
                .body(resource);
    }

}
