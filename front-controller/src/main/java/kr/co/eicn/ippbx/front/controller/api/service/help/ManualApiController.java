package kr.co.eicn.ippbx.front.controller.api.service.help;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.ManualForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.help.ManualApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.BoardSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ManualDetailResponse;
import kr.co.eicn.ippbx.model.search.BoardSearchRequest;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/manual", produces = MediaType.APPLICATION_JSON_VALUE)
public class ManualApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ManualApiController.class);

    @Autowired
    private ManualApiInterface apiInterface;

    @GetMapping
    public Pagination<BoardSummaryResponse> pagination(BoardSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{id}")
    public ManualDetailResponse get(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @GetMapping("{id}/detail")
    public ManualDetailResponse getDetail(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.getDetail(id);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody ManualForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PostMapping("{id}")
    public void put(@Valid @RequestBody ManualForm form, BindingResult bindingResult, @PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }

    @ResponseBody
    @GetMapping("id/{id}/resource")
    public ResponseEntity<byte[]> getResoucreInManul(@PathVariable Integer id) throws IOException, ResultFailException {
        Resource resource = apiInterface.getResource(id);
        byte[] bytes = IOUtils.toByteArray(resource.getInputStream());

        HttpHeaders headers = new HttpHeaders();
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
