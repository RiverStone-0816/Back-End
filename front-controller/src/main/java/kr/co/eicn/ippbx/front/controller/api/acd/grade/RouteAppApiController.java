package kr.co.eicn.ippbx.front.controller.api.acd.grade;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.grade.RouteAppApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.RouteApplicationEntity;
import kr.co.eicn.ippbx.model.form.RAFormUpdateRequest;
import kr.co.eicn.ippbx.model.form.RouteApplicationFormRequest;
import kr.co.eicn.ippbx.model.search.RouteApplicationSearchRequest;
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
@RequestMapping(value = "api/route-app", produces = MediaType.APPLICATION_JSON_VALUE)
public class RouteAppApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RouteAppApiController.class);

    @Autowired
    private RouteAppApiInterface apiInterface;

    @GetMapping("")
    public Pagination<RouteApplicationEntity> pagination(RouteApplicationSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("person")
    public List<SearchPersonListResponse> person() throws IOException, ResultFailException {
        return apiInterface.person();
    }

    @GetMapping("{seq}")
    public RouteApplicationEntity get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody RouteApplicationFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{seq}/accept")
    public void accept(@Valid @RequestBody RAFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.accept(seq, form);
    }

    @PutMapping("{seq}/reject")
    public void reject(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.reject(seq);
    }
}
