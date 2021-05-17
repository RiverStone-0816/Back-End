package kr.co.eicn.ippbx.front.controller.api.talk;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.TalkTemplateApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.TalkTemplateFormRequest;
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
@RequestMapping(value = "api/talk-template", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkTemplateApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkTemplateApiController.class);

    @Autowired
    private TalkTemplateApiInterface apiInterface;

    @GetMapping("")
    public List<TalkTemplateSummaryResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{seq}")
    public TalkTemplateSummaryResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody TalkTemplateFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody TalkTemplateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }
}
