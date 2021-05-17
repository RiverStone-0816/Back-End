package kr.co.eicn.ippbx.front.controller.api.application.mail;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.mail.MailCategoryApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendFaxEmailCategoryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.SendCategoryUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.SendFaxEmailCategoryFormRequest;
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
@RequestMapping(value = "api/mail-category", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailCategoryApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(MailCategoryApiController.class);

    @Autowired
    private MailCategoryApiInterface apiInterface;

    @GetMapping("")
    public Pagination<SendFaxEmailCategoryResponse> pagination(SendCategorySearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping(value = "{categoryCode}")
    public SendFaxEmailCategoryResponse get(@PathVariable String categoryCode) throws IOException, ResultFailException {
        return apiInterface.get(categoryCode);
    }

    @PostMapping("")
    public String post(@Valid @RequestBody SendFaxEmailCategoryFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping(value = "{categoryCode}")
    public void put(@PathVariable String categoryCode, @Valid @RequestBody SendCategoryUpdateRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.put(categoryCode, form);
    }

    @DeleteMapping(value = "{categoryCode}")
    public void delete(@PathVariable String categoryCode) throws IOException, ResultFailException {
        apiInterface.delete(categoryCode);
    }

    @GetMapping("category")
    public List<SendSmsCategorySummaryResponse> sendCategory() throws IOException, ResultFailException {
        return apiInterface.sendCategory();
    }
}
