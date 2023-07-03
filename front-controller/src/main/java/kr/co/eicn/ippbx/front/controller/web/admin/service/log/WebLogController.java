package kr.co.eicn.ippbx.front.controller.web.admin.service.log;

import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.log.WebLogApiInterface;
import kr.co.eicn.ippbx.front.service.excel.WebLogExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.WebSecureHistoryResponse;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.model.search.WebSecureHistorySearchRequest;
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
@RequestMapping("admin/service/log/web-log")
public class WebLogController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WebLogController.class);

    @Autowired
    private WebLogApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") WebSecureHistorySearchRequest search) throws IOException, ResultFailException {
        final int limit = 10000;

        final Pagination<WebSecureHistoryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("limit", limit);
        model.addAttribute("actionTypes", FormUtils.optionsOfCode(WebSecureActionType.class));
        model.addAttribute("actionSubTypes", FormUtils.optionsOfCode(WebSecureActionSubType.class));

        return "admin/service/log/web-log/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(WebSecureHistorySearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new WebLogExcel(search, apiInterface.pagination(search).getRows()).generator(response, "웹로그");
    }
}
