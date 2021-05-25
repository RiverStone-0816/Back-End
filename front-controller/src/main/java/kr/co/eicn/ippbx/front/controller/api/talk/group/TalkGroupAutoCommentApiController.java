package kr.co.eicn.ippbx.front.controller.api.talk.group;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkGroupAutoCommentApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMentDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMentSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMentFormRequest;
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
@RequestMapping(value = "api/talk-auto-comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkGroupAutoCommentApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkGroupAutoCommentApiController.class);

    @Autowired
    private TalkGroupAutoCommentApiInterface apiInterface;

    @GetMapping("")
    public List<TalkMentSummaryResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{seq}")
    public TalkMentDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody TalkMentFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody TalkMentFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
