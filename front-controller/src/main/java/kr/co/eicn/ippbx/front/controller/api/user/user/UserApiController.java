package kr.co.eicn.ippbx.front.controller.api.user.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.form.PersonFormRequest;
import kr.co.eicn.ippbx.model.form.PersonFormUpdateRequest;
import kr.co.eicn.ippbx.model.form.PersonPasswordUpdateRequest;
import kr.co.eicn.ippbx.model.search.PersonSearchRequest;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */

@Api(description = "사용자 정보", tags = {"USER"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    private UserApiInterface apiInterface;

    @LoginRequired
    @GetMapping("")
    public Pagination<PersonSummaryResponse> pagination(PersonSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @LoginRequired
    @GetMapping("{id}")
    public PersonDetailResponse get(@PathVariable String id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    @LoginRequired
    @PostMapping("")
    public void post(@RequestBody @Valid PersonFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @LoginRequired
    @PutMapping("{id}")
    public void update(@PathVariable String id, @RequestBody @Valid PersonFormUpdateRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.update(id, form);
    }

    @LoginRequired
    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) throws IOException, ResultFailException {
        apiInterface.delete(id);
    }

    @LoginRequired
    @PostMapping("data-upload")
    public void post(@RequestBody @Valid List<PersonFormRequest> list, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(list);
    }

    @LoginRequired
    @PatchMapping("{id}/password")
    public void updatePassword(@PathVariable String id, @RequestBody @Valid PersonPasswordUpdateRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.updatePassword(id, form);
    }

    @LoginRequired
    @GetMapping("is-id-available")
    public void isIdAvailable(@RequestParam String userId) throws IOException, ResultFailException {
        apiInterface.isIdAvailable(userId);
    }

    @LoginRequired
    @GetMapping("extensions")
    public List<SummaryPhoneInfoResponse> extensions(@RequestParam(required = false) String extension) throws IOException, ResultFailException {
        return apiInterface.extensions(extension);
    }

    @GetMapping("session-check")
    public JSessionInfo sessionCheck(){
        final PersonDetailResponse user = g.getUser();
        if(ObjectUtils.isEmpty(user))
            return JSessionInfo.builder().jsessionid("").companyId("").adminId("").build();

        return JSessionInfo.builder().jsessionid(g.getSessionId()).companyId(user.getCompanyId()).adminId(user.getId()).build();
    }

    @Builder
    @Data
    public static class JSessionInfo {
        @JsonProperty("COMPANY_ID")
        private String companyId;
        @JsonProperty("JSESSIONID")
        private String jsessionid;
        @JsonProperty("ADMIN_ID")
        private String adminId;
    }
}
