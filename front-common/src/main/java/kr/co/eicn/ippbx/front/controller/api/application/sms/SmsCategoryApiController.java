package kr.co.eicn.ippbx.front.controller.api.application.sms;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsCategoryApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategoryDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.model.form.SendCategoryUpdateRequest;
import kr.co.eicn.ippbx.model.form.SendSmsCategoryFormRequest;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
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
@RequestMapping(value = "api/sms-category", produces = MediaType.APPLICATION_JSON_VALUE)
public class SmsCategoryApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SmsCategoryApiController.class);

    @Autowired
    private SmsCategoryApiInterface apiInterface;

    @GetMapping("")
    public Pagination<SendSmsCategoryDetailResponse> pagination(SendCategorySearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping(value = "{categoryCode}")
    public SendSmsCategoryDetailResponse get(@PathVariable String categoryCode) throws IOException, ResultFailException {
        return apiInterface.get(categoryCode);
    }

    @PostMapping("")
    public String post(@Valid @RequestBody SendSmsCategoryFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
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
