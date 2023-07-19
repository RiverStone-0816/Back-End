package kr.co.eicn.ippbx.front.controller.api.wtalk;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.wtalk.WtalkTemplateApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
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
import java.util.List;
import java.util.Objects;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/wtalk-template", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkTemplateApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkTemplateApiController.class);

    @Autowired
    private WtalkTemplateApiInterface wtalkTemplateApiInterface;

    @GetMapping("")
    public List<WtalkTemplateSummaryResponse> list(TemplateSearchRequest search) throws IOException, ResultFailException {
        return wtalkTemplateApiInterface.list(search);
    }

    @GetMapping("my")
    public List<WtalkTemplateSummaryResponse> myList(TemplateSearchRequest search) throws IOException, ResultFailException {
        search.setIsMy(true);
        return wtalkTemplateApiInterface.list(search);
    }

    /*@GetMapping("")
    public Pagination<TalkTemplateSummaryResponse> getPagination(TemplateSearchRequest form) throws IOException, ResultFailException {
        return apiInterface.getPagination(form);
    }*/

    @GetMapping("{seq}")
    public WtalkTemplateSummaryResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return wtalkTemplateApiInterface.get(seq);
    }

    @PostMapping("")
    public String post(@Valid @RequestBody WtalkTemplateApiInterface.TemplateForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        return wtalkTemplateApiInterface.post(form, g.getUser().getCompanyId());
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody WtalkTemplateApiInterface.TemplateForm form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        wtalkTemplateApiInterface.put(seq, form, g.getUser().getCompanyId());
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        wtalkTemplateApiInterface.delete(seq);
    }

    @ResponseBody
    @GetMapping("resource")
    public ResponseEntity<byte[]> getResoucreInWtalk(@RequestParam String filePath) throws IOException, ResultFailException {
        Resource resource = wtalkTemplateApiInterface.getResource(filePath);
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
