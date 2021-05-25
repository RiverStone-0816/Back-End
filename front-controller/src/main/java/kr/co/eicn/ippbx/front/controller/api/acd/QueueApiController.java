package kr.co.eicn.ippbx.front.controller.api.acd;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.QueueDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.QueueSummaryResponse;
import kr.co.eicn.ippbx.model.form.QueueFormRequest;
import kr.co.eicn.ippbx.model.form.QueueFormUpdateRequest;
import kr.co.eicn.ippbx.model.form.QueueUpdateBlendingFormRequest;
import kr.co.eicn.ippbx.model.search.QueueSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/queue", produces = MediaType.APPLICATION_JSON_VALUE)
public class QueueApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QueueApiController.class);

    @Autowired
    private QueueApiInterface apiInterface;

    @GetMapping("")
    public Pagination<QueueSummaryResponse> pagination(QueueSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{name}")
    public QueueDetailResponse get(@PathVariable String name) throws IOException, ResultFailException {
        return apiInterface.get(name);
    }

    @PostMapping("")
    public String post(@Valid @RequestBody QueueFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{name}")
    public void put(@Valid @RequestBody QueueFormUpdateRequest form, BindingResult bindingResult, @PathVariable String name) throws IOException, ResultFailException {
        apiInterface.update(name, form);
    }

    @PatchMapping("{name}/blending")
    public void patchBlending(@Valid @RequestBody QueueUpdateBlendingFormRequest form, BindingResult bindingResult, @PathVariable String name) throws IOException, ResultFailException {
        apiInterface.updateBlending(name, form);
    }

    @DeleteMapping("{name}")
    public void delete(@PathVariable String name) throws IOException, ResultFailException {
        apiInterface.delete(name);
    }
}
