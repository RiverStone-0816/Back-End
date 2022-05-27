package kr.co.eicn.ippbx.front.controller.api.wtalk.group;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMemberGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMemberGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMemberGroupFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
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
@RequestMapping(value = "api/wtalk-reception-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class WtalkReceptionGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkReceptionGroupApiController.class);

    @Autowired
    private WtalkReceptionGroupApiInterface apiInterface;

    @GetMapping("")
    public List<WtalkMemberGroupSummaryResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{groupId}")
    public WtalkMemberGroupDetailResponse get(@PathVariable Integer groupId) throws IOException, ResultFailException {
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
