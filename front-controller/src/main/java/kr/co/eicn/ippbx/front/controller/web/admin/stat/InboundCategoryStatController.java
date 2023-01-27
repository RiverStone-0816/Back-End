package kr.co.eicn.ippbx.front.controller.web.admin.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.InboundCategoryStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.InboundCategoryStatExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryIvrTreeResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatCategoryResponse;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatCategorySearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
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
@RequestMapping("admin/stat/inbound/category")
public class InboundCategoryStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(InboundCategoryStatController.class);

    @Autowired
    private InboundCategoryStatApiInterface apiInterface;
    @Autowired
    private SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") StatCategorySearchRequest search) throws IOException, ResultFailException {
        final List<StatCategoryResponse<?>> stat = apiInterface.list(search);
        model.addAttribute("stat", stat);

        final Integer maxLevel = stat.stream().map(StatCategoryResponse::getMaxLevel).max(Integer::compareTo).orElse(0);
        model.addAttribute("maxLevel", maxLevel);

        final Map<String, String> searchCycles = FormUtils.options(false, SearchCycle.class);
        model.addAttribute("searchCycles", searchCycles);

        final Map<String, String> services = searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));
        model.addAttribute("services", services);

        final Map<String, String> ivrNodes = apiInterface.ivrTree().stream().collect(Collectors.toMap(StatCategoryIvrTreeResponse::getTreeName, StatCategoryIvrTreeResponse::getName));
        model.addAttribute("ivrNodes", ivrNodes);

        return "admin/stat/inbound/category/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(StatCategorySearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatCategoryResponse<?>> stat = apiInterface.list(search);
        final Integer maxLevel = stat.stream().map(StatCategoryResponse::getMaxLevel).max(Integer::compareTo).orElse(0);

        new InboundCategoryStatExcel(stat, maxLevel).generator(response, "인입경로별통계");
    }
}
