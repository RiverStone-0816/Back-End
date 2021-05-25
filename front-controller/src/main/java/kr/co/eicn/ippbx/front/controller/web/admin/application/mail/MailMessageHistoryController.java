package kr.co.eicn.ippbx.front.controller.web.admin.application.mail;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.mail.MailHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.MailHistoryExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendFaxEmailHistoryResponse;
import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.model.search.SendFaxEmailHistorySearchRequest;
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

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/application/mail/history")
public class MailMessageHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MailMessageHistoryController.class);

    @Autowired
    private MailHistoryApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") SendFaxEmailHistorySearchRequest search) throws IOException, ResultFailException {
        final Pagination<SendFaxEmailHistoryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("sendCategoryTypes", FormUtils.optionsOfCode(SendCategoryType.EMAIL, SendCategoryType.FAX));

        return "admin/application/mail/history/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(SendFaxEmailHistorySearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new MailHistoryExcel(search, apiInterface.pagination(search).getRows()).generator(response, "FAX-EMAIL이력");
    }
}
