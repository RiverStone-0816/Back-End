package kr.co.eicn.ippbx.front.controller.web.admin.service.log;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.WebSecureHistorySearch;
import kr.co.eicn.ippbx.front.service.ResultFailException;
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
import java.sql.Timestamp;
import java.util.Calendar;

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
    public String page(Model model, @ModelAttribute("search") WebSecureHistorySearch search) throws IOException, ResultFailException {
        final int limit = 10000;
        final WebSecureHistorySearchRequest searchRequest = getWebSecureHistorySearchRequest(search);
        final Pagination<WebSecureHistoryResponse> pagination = apiInterface.pagination(searchRequest);
        model.addAttribute("pagination", pagination);
        model.addAttribute("limit", limit);
        model.addAttribute("actionTypes", FormUtils.optionsOfCode(WebSecureActionType.class));

        return "admin/service/log/web-log/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(WebSecureHistorySearch search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        final WebSecureHistorySearchRequest searchRequest = getWebSecureHistorySearchRequest(search);
        new WebLogExcel(searchRequest, apiInterface.pagination(searchRequest).getRows()).generator(response, "웹로그");
    }

    private WebSecureHistorySearchRequest getWebSecureHistorySearchRequest(WebSecureHistorySearch search) {
        final WebSecureHistorySearchRequest searchRequest = new WebSecureHistorySearchRequest();
        ReflectionUtils.copy(searchRequest, search);

        if (search.getStartDate() != null && search.getStartHour() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(search.getStartDate());
            calendar.set(Calendar.HOUR_OF_DAY, search.getStartHour());
            searchRequest.setStartTimestamp(new Timestamp(calendar.getTimeInMillis()));
        }

        if (search.getEndDate() != null && search.getEndHour() != null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(search.getEndDate());
            calendar.set(Calendar.HOUR_OF_DAY, search.getEndHour());
            searchRequest.setEndTimestamp(new Timestamp(calendar.getTimeInMillis()));
        }

        return searchRequest;
    }
}
