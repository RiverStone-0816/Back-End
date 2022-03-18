package kr.co.eicn.ippbx.front.controller.api.talk;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.TalkTemplateApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
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
public class TalkTemplateApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkTemplateApiController.class);

    @Autowired
    private TalkTemplateApiInterface talkTemplateApiInterface;

    @GetMapping("")
    public List<TalkTemplateSummaryResponse> list(TemplateSearchRequest search) throws IOException, ResultFailException {
        return talkTemplateApiInterface.list(search);
    }

    @GetMapping("my")
    public List<TalkTemplateSummaryResponse> myList(TemplateSearchRequest search) throws IOException, ResultFailException {
        search.setIsMy(true);
        return talkTemplateApiInterface.list(search);
    }

    /*@GetMapping("")
    public Pagination<TalkTemplateSummaryResponse> getPagination(TemplateSearchRequest form) throws IOException, ResultFailException {
        return apiInterface.getPagination(form);
    }*/

    @GetMapping("{seq}")
    public TalkTemplateSummaryResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return talkTemplateApiInterface.get(seq);
    }

    @PostMapping("")
    public String post(@Valid @RequestBody TalkTemplateApiInterface.TemplateForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        return talkTemplateApiInterface.post(form, g.getUser().getCompanyId());
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody TalkTemplateApiInterface.TemplateForm form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        talkTemplateApiInterface.put(seq, form, g.getUser().getCompanyId());
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        talkTemplateApiInterface.delete(seq);
    }
}
