package kr.co.eicn.ippbx.front.controller.web.admin.record.callback;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.callback.CallbackHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.CallbackHistoryExcel;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.CallbackHistoryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryCallbackDistPersonResponse;
import kr.co.eicn.ippbx.server.model.enums.CallbackStatus;
import kr.co.eicn.ippbx.server.model.form.CallbackListUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.form.CallbackRedistFormRequest;
import kr.co.eicn.ippbx.server.model.search.CallbackHistorySearchRequest;
import kr.co.eicn.ippbx.server.model.search.search.SearchServiceRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/record/callback/history")
public class CallbackHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CallbackHistoryController.class);

    private final CallbackHistoryApiInterface apiInterface;
    private final SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") CallbackHistorySearchRequest search) throws IOException, ResultFailException {
        final Pagination<CallbackHistoryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final LinkedHashMap<String, String> callbackStatusOptions = FormUtils.options(CallbackStatus.class);
        model.addAttribute("callbackStatusOptions", callbackStatusOptions);

        final Map<String, String> serviceOptions = new HashMap<>();
        searchApiInterface.services(new SearchServiceRequest()).forEach(e -> serviceOptions.put(e.getSvcNumber(), e.getSvcName()));
        model.addAttribute("serviceOptions", serviceOptions);

        return "admin/record/callback/history/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(CallbackHistorySearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new CallbackHistoryExcel(search, apiInterface.pagination(search).getRows()).generator(response, "콜백이력");
    }

    @GetMapping("modal-redistribution")
    public String modalRedistribution(Model model, @ModelAttribute("form") CallbackRedistFormRequest form) throws IOException, ResultFailException {
        final List<SummaryCallbackDistPersonResponse> addOnPersons = apiInterface.addPersons();
        model.addAttribute("addOnPersons", addOnPersons);

        return "admin/record/callback/history/modal-redistribution";
    }

    @GetMapping("modal-process")
    public String modalProcess(Model model, @ModelAttribute("form") CallbackListUpdateFormRequest form) {
        final LinkedHashMap<String, String> callbackStatusOptions = FormUtils.options(false, CallbackStatus.class);
        model.addAttribute("callbackStatusOptions", callbackStatusOptions);

        return "admin/record/callback/history/modal-process";
    }
}
