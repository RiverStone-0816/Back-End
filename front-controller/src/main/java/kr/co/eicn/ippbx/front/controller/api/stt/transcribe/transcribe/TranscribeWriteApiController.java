package kr.co.eicn.ippbx.front.controller.api.stt.transcribe.transcribe;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.stt.transcribe.TranscribeWriteApiInterface;
import kr.co.eicn.ippbx.model.form.TranscribeWriteFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/transcribe/write", produces = MediaType.APPLICATION_JSON_VALUE)
public class TranscribeWriteApiController extends BaseController {
    private final TranscribeWriteApiInterface transcribeWriteApiInterface;

    @PutMapping("{seq}")
    public String update(@PathVariable Integer seq, @RequestBody TranscribeWriteFormRequest request) throws IOException, ResultFailException {
        return transcribeWriteApiInterface.update(seq, request);
    }

    @ResponseBody
    @GetMapping("{fileSeq}/resource")
    public ResponseEntity<byte[]> getResource(@PathVariable Integer fileSeq) throws IOException, ResultFailException {
        Resource resource = transcribeWriteApiInterface.getResources(fileSeq);
        byte[] bytes = IOUtils.toByteArray(resource.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("audio", "mpeg"));
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment")
                        .filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8)
                        .build().toString());
        headers.setPragma("no-cache");
        headers.setCacheControl(CacheControl.noCache());
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.setContentLength(bytes.length);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
