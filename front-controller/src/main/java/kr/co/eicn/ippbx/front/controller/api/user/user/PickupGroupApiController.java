package kr.co.eicn.ippbx.front.controller.api.user.user;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.user.PickupGroupApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.PickUpGroupSearchRequest;
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
@RequestMapping(value = "api/pickup-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PickupGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PickupGroupApiController.class);

    @Autowired
    private PickupGroupApiInterface apiInterface;

    @GetMapping("")
    public Pagination<PickUpGroupSummaryResponse> pagination(PickUpGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{groupCode}")
    public PickUpGroupDetailResponse get(@PathVariable Integer groupCode) throws IOException, ResultFailException {
        return apiInterface.get(groupCode);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody PickUpGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{groupCode}")
    public void put(@Valid @RequestBody PickUpGroupFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer groupCode) throws IOException, ResultFailException {
        apiInterface.update(groupCode, form);
    }

    @DeleteMapping("{groupCode}")
    public void delete(@PathVariable Integer groupCode) throws IOException, ResultFailException {
        apiInterface.delete(groupCode);
    }
}
