package kr.co.eicn.ippbx.front.controller.api.talk.group;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMemberGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkMemberGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
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
@RequestMapping(value = "api/talk-reception-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class TalkReceptionGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkReceptionGroupApiController.class);

    @Autowired
    private TalkReceptionGroupApiInterface apiInterface;

    @GetMapping("")
    public List<TalkMemberGroupSummaryResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{groupId}")
    public TalkMemberGroupDetailResponse get(@PathVariable Integer groupId) throws IOException, ResultFailException {
        return apiInterface.get(groupId);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody TalkMemberGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{groupId}")
    public void put(@Valid @RequestBody TalkMemberGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer groupId) throws IOException, ResultFailException {
        apiInterface.put(groupId, form);
    }

    @DeleteMapping("{groupId}")
    public void delete(@PathVariable Integer groupId) throws IOException, ResultFailException {
        apiInterface.delete(groupId);
    }
}
