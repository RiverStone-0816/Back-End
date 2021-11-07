package kr.co.eicn.ippbx.front.controller.web.admin.service.log;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.log.WebLoginHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.LoginHistoryExcel;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.LoginHistoryResponse;
import kr.co.eicn.ippbx.model.search.LoginHistorySearchRequest;
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
@RequestMapping("admin/service/log/login-history")
public class WebLoginHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WebLoginHistoryController.class);

    @Autowired
    private WebLoginHistoryApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") LoginHistorySearchRequest search) throws IOException, ResultFailException {
        final Pagination<LoginHistoryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/service/log/login-history/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(LoginHistorySearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new LoginHistoryExcel(search, apiInterface.pagination(search).getRows()).generator(response, "로그인이력");
    }
}
