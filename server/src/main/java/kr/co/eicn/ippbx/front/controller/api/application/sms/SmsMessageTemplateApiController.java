package kr.co.eicn.ippbx.front.controller.api.application.sms;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsMessageTemplateApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendMessageTemplateResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.SendMessageTemplateUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.SendMessageTemplateFormRequest;
import kr.co.eicn.ippbx.server.model.search.SendMessageTemplateSearchRequest;
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
@RequestMapping(value = "api/sms-message-template", produces = MediaType.APPLICATION_JSON_VALUE)
public class SmsMessageTemplateApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageTemplateApiController.class);

    @Autowired
    private SmsMessageTemplateApiInterface apiInterface;

    @GetMapping("")
    public Pagination<SendMessageTemplateResponse> pagination(SendMessageTemplateSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping(value = "{id}")
    public SendMessageTemplateResponse get(@PathVariable Integer id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @PostMapping("")
    public Long post(@Valid @RequestBody SendMessageTemplateFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping(value = "{id}")
    public void put(@PathVariable Integer id, @Valid @RequestBody SendMessageTemplateUpdateRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable Integer id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }

    @GetMapping("category")
    public List<SendSmsCategorySummaryResponse> sendCategory() throws IOException, ResultFailException {
        return apiInterface.sendCategory();
    }
}
