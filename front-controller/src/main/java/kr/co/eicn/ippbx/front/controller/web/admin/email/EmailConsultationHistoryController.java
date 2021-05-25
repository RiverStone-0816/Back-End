package kr.co.eicn.ippbx.front.controller.web.admin.email;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.api.user.user.UserApiController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.email.EmailConsultationHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.EmailConsultationHistoryExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.EmailConsultationHistorySummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.enums.ResultCodeType;
import kr.co.eicn.ippbx.model.search.EmailConsultationHistorySearchRequest;
import kr.co.eicn.ippbx.model.search.PersonSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/email/consultation-history")
public class EmailConsultationHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EmailConsultationHistoryController.class);

    @Autowired
    private EmailConsultationHistoryApiInterface apiInterface;
    @Autowired
    private UserApiController userApiController;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") EmailConsultationHistorySearchRequest search) throws IOException, ResultFailException {
        final Pagination<EmailConsultationHistorySummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> resultCodeTypes = FormUtils.options(ResultCodeType.class);
        model.addAttribute("resultCodeTypes", resultCodeTypes);

        final PersonSearchRequest personSearchRequest = new PersonSearchRequest();
        personSearchRequest.setLimit(1000);
        final Map<String, String> users = userApiController.pagination(personSearchRequest).getRows().stream().collect(Collectors.toMap(PersonSummaryResponse::getId, PersonSummaryResponse::getIdName));
        model.addAttribute("users", users);

        return "admin/email/consultation-history/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(EmailConsultationHistorySearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new EmailConsultationHistoryExcel(search, apiInterface.pagination(search).getRows()).generator(response, "이메일상담이력");
    }
}
