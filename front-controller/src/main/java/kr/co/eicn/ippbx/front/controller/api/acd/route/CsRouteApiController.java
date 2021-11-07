package kr.co.eicn.ippbx.front.controller.api.acd.route;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.route.CsRouteApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.CsRouteGetResponse;
import kr.co.eicn.ippbx.model.dto.eicn.CsRouteSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.form.CsRouteFormRequest;
import kr.co.eicn.ippbx.model.search.CsRouteSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
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
@RequestMapping(value = "api/cs-route", produces = MediaType.APPLICATION_JSON_VALUE)
public class CsRouteApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CsRouteApiController.class);

    @Autowired
    private CsRouteApiInterface apiInterface;

    @GetMapping
    public Pagination<CsRouteSummaryResponse> pagination(CsRouteSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }


    //큐목록불러오기
    @GetMapping("queue")
    public List<SearchQueueResponse> queue() throws IOException, ResultFailException {
        return apiInterface.queue();
    }

    //수정을 위한 SEQ조회
    @GetMapping("{seq}")
    public CsRouteGetResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    //추가
    @PostMapping("")
    public Integer post(@Valid @RequestBody CsRouteFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    //수정
    @PutMapping("{seq}")
    public void put(@Valid @RequestBody CsRouteFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    //삭제
    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

}
