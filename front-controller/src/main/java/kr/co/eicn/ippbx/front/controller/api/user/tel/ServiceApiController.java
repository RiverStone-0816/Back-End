package kr.co.eicn.ippbx.front.controller.api.user.tel;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.tel.ServiceApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListSummaryResponse;
import kr.co.eicn.ippbx.model.form.ServiceListFormRequest;
import kr.co.eicn.ippbx.model.form.ServiceListFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.ServiceListSearchRequest;
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
@RequestMapping(value = "api/service", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceApiController.class);

    @Autowired
    private ServiceApiInterface apiInterface;

    @GetMapping("")
    public List<ServiceListSummaryResponse> list(ServiceListSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list(search);
    }

    @GetMapping("{seq}")
    public ServiceListDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody ServiceListFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void post(@Valid @RequestBody ServiceListFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.update(seq, form);
    }

    @DeleteMapping("{seq}")
    public void post(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
