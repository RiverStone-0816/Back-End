package kr.co.eicn.ippbx.front.controller.web.admin.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.route.CsRouteApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.OutboundStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.OutboundStatExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatOutboundResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatOutboundTimeResponse;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.StatOutboundSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stat/outbound/outbound")
public class OutboundStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundStatController.class);

    private final OutboundStatApiInterface apiInterface;
    private final SearchApiInterface searchApiInterface;
    private final CsRouteApiInterface csRouteApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") StatOutboundSearchRequest search) throws IOException, ResultFailException {
        final List<StatOutboundTimeResponse<?>> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final StatOutboundResponse total = apiInterface.total(search);
        model.addAttribute("total", total);

        final Map<String, String> searchCycles = FormUtils.options(false, SearchCycle.class);
        model.addAttribute("searchCycles", searchCycles);

        final Map<String, String> services = searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));
        model.addAttribute("services", services);

        final Map<String, String> queues = csRouteApiInterface.queue().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        return "admin/stat/outbound/outbound/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(StatOutboundSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatOutboundTimeResponse<?>> list = apiInterface.list(search);
        final StatOutboundResponse total = apiInterface.total(search);
        new OutboundStatExcel(list, total).generator(response, "아웃바운드통계");
    }
}
