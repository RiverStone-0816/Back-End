package kr.co.eicn.ippbx.front.controller.web.admin.application.sms;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsMessageHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.SmsMessageHistoryExcel;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendMessageHistoryResponse;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;
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
@RequestMapping("admin/application/sms/message-history")
public class SmsMessageHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageHistoryController.class);

    @Autowired
    private SmsMessageHistoryApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") SendMessageSearchRequest search) throws IOException, ResultFailException {
        final Pagination<SendMessageHistoryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/application/sms/message-history/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(SendMessageSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new SmsMessageHistoryExcel(search, apiInterface.pagination(search).getRows()).generator(response, "SMS이력");
    }
}
