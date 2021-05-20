package kr.co.eicn.ippbx.front.controller.api.service.help;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.NoticeForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.help.NoticeApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.BoardSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.NoticeDetailResponse;
import kr.co.eicn.ippbx.model.search.BoardSearchRequest;
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
@RequestMapping(value = "api/notice", produces = MediaType.APPLICATION_JSON_VALUE)
public class NoticeApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeApiController.class);

    @Autowired
    private NoticeApiInterface apiInterface;

    @GetMapping
    public Pagination<BoardSummaryResponse> pagination(BoardSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{id}")
    public NoticeDetailResponse get(@PathVariable Long id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody NoticeForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PostMapping("{id}")
    public void put(@Valid @RequestBody NoticeForm form, BindingResult bindingResult, @PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.put(id, form);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }
}
