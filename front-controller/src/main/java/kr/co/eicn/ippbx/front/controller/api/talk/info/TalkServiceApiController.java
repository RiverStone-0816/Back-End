package kr.co.eicn.ippbx.front.controller.api.talk.info;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.info.TalkServiceApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkServiceSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
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
@RequestMapping(value = "api/talk-service", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkServiceApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkServiceApiController.class);

    @Autowired
    private TalkServiceApiInterface apiInterface;

    @GetMapping("")
    public List<TalkServiceSummaryResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{seq}")
    public TalkServiceDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody TalkServiceInfoFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody TalkServiceInfoFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
