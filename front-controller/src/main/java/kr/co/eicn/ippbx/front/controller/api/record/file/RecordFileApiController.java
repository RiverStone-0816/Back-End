package kr.co.eicn.ippbx.front.controller.api.record.file;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.file.RecordFileApiInterface;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/record-file", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordFileApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileApiController.class);

    @Autowired
    private RecordFileApiInterface apiInterface;

    @DeleteMapping("{fileName}")
    public void delete(@PathVariable String fileName) throws IOException, ResultFailException {
        apiInterface.delete(fileName);
    }

    @ResponseBody
    @GetMapping("resource")
    public ResponseEntity<byte[]> get(@RequestParam String path, @RequestParam String mode) throws IOException, ResultFailException {
        Resource resource = apiInterface.getResource(path, mode);
        byte[] bytes = IOUtils.toByteArray(resource.getInputStream());

        HttpHeaders headers = new HttpHeaders();
        if ("PLAY".equals(mode))
            headers.setContentType(new MediaType("audio", "mpeg"));
        else if ("DOWN".equals(mode))
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
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
