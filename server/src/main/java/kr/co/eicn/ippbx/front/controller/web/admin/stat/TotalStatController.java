package kr.co.eicn.ippbx.front.controller.web.admin.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.TotalStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.TotalStatExcel;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatTotalRow;
import kr.co.eicn.ippbx.server.model.dto.util.DateResponse;
import kr.co.eicn.ippbx.server.model.enums.SearchCycle;
import kr.co.eicn.ippbx.server.model.search.StatTotalSearchRequest;
import kr.co.eicn.ippbx.server.model.search.search.SearchServiceRequest;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/stat/total")
public class TotalStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TotalStatController.class);

    @Autowired
    private TotalStatApiInterface apiInterface;
    @Autowired
    private SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") StatTotalSearchRequest search) throws IOException, ResultFailException {
        final List<StatTotalRow<?>> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final StatTotalRow<DateResponse> total = apiInterface.total(search);
        model.addAttribute("total", total);

        final Map<String, String> searchCycles = FormUtils.options(false, SearchCycle.class);
        model.addAttribute("searchCycles", searchCycles);

        final Map<String, String> services = searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));
        model.addAttribute("services", services);

        return "admin/stat/total/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(StatTotalSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatTotalRow<?>> stat = apiInterface.list(search);
        final StatTotalRow<DateResponse> total = apiInterface.total(search);
        new TotalStatExcel(stat, total).generator(response, "총통화통계");
    }
}
