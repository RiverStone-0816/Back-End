package kr.co.eicn.ippbx.front.controller.api.email;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.email.EmailConsultationHistoryApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonCodeResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.EmailConsultationHistorySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.EmailConsultationHistoryFormRequest;
import kr.co.eicn.ippbx.server.model.search.EmailConsultationHistorySearchRequest;
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
@RequestMapping(value = "api/email-consultation-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailConsultationHistoryApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(EmailConsultationHistoryApiController.class);

    @Autowired
    private EmailConsultationHistoryApiInterface apiInterface;

    @GetMapping("")
    public Pagination<EmailConsultationHistorySummaryResponse> pagination(EmailConsultationHistorySearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @PutMapping("redistribution")
    public void put(@Valid @RequestBody EmailConsultationHistoryFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.redistribution(form);
    }

    @DeleteMapping("common-code")
    public List<CommonCodeResponse> delete() throws IOException, ResultFailException {
        return apiInterface.emailCommonCode();
    }
}
