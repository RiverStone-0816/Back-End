package kr.co.eicn.ippbx.front.controller.api.application.mail;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.model.form.MailFileForm;
import kr.co.eicn.ippbx.front.model.form.MailFileUpdateForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.mail.MailFileApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendFileResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.server.model.search.SendCategorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/mail-file", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailFileApiController extends ApiBaseController {
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

}
