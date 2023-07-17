package kr.co.eicn.ippbx.front.controller.api.application.mail;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.MailFileForm;
import kr.co.eicn.ippbx.front.model.form.MailFileUpdateForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.mail.MailFileApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendFileResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
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
@RequestMapping(value = "api/mail-file", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailFileApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MailFileApiController.class);

    @Autowired
    private MailFileApiInterface apiInterface;

    /**
     * 발송물관리 목록조회
     */
    @GetMapping("")
    public Pagination<SendFileResponse> pagination(SendCategorySearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    /**
     * 발송물관리 상세조회
     */
    @GetMapping(value = "{id}")
    public SendFileResponse get(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    /**
     * 발송물관리 추가
     */
    @PostMapping("")
    public Long post(@Valid @RequestBody MailFileForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    /**
     * 발송물관리 수정
     */
    @PutMapping("{id}")
    public void put(@Valid @RequestBody MailFileUpdateForm form, BindingResult bindingResult, @PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    /**
     * 발송물관리 삭제
     */
    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }

    /**
     * 카테고리 조회
     */
    @GetMapping("category")
    public List<SendSmsCategorySummaryResponse> sendFaxEmailCategory() throws IOException, ResultFailException {
        return apiInterface.sendCategory();
    }

    @ResponseBody
    @GetMapping("id/{id}/resource")
    public ResponseEntity<byte[]> getResoucreInFax(@PathVariable Long id) throws IOException, ResultFailException {
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
