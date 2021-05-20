package kr.co.eicn.ippbx.front.controller.api.email;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.email.EmailReceptionGroupApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMemberListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMngSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailReceiveGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.EmailReceiveGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.EmailReceiveGroupFormRequest;
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
@RequestMapping(value = "api/email-reception-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailReceptionGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EmailReceptionGroupApiController.class);

    @Autowired
    private EmailReceptionGroupApiInterface apiInterface;

    @GetMapping("")
    public List<EmailReceiveGroupSummaryResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{groupId}")
    public EmailReceiveGroupDetailResponse get(@PathVariable Integer groupId) throws IOException, ResultFailException {
        return apiInterface.get(groupId);
    }

    @PostMapping("")
    public Integer post(@Valid @RequestBody EmailReceiveGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{groupId}")
    public void put(@Valid @RequestBody EmailReceiveGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer groupId) throws IOException, ResultFailException {
        apiInterface.put(groupId, form);
    }

    @DeleteMapping("{groupId}")
    public void delete(@PathVariable Integer groupId) throws IOException, ResultFailException {
        apiInterface.delete(groupId);
    }

    @GetMapping("services")
    public List<EmailMngSummaryResponse> services() throws IOException, ResultFailException {
        return apiInterface.services();
    }

    @GetMapping("available-members")
    public List<EmailMemberListSummaryResponse> availableMembers(@RequestParam(required = false) Integer groupId) throws IOException, ResultFailException {
        return apiInterface.availableMembers(groupId);
    }
}
