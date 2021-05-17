package kr.co.eicn.ippbx.front.controller.web.admin.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.route.CsRouteApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.InboundStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.InboundStatExcel;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchQueueNameResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatInboundResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatInboundTimeResponse;
import kr.co.eicn.ippbx.server.model.enums.SearchCycle;
import kr.co.eicn.ippbx.server.model.search.StatInboundSearchRequest;
import kr.co.eicn.ippbx.server.model.search.search.SearchQueueNameRequest;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stat/inbound/inbound")
public class InboundStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(InboundStatController.class);

    private final InboundStatApiInterface apiInterface;
    private final SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") StatInboundSearchRequest search) throws IOException, ResultFailException {
        final List<StatInboundTimeResponse<?>> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final StatInboundResponse total = apiInterface.total(search);
        model.addAttribute("total", total);

        final Map<String, String> searchCycles = FormUtils.options(false, SearchCycle.class);
        model.addAttribute("searchCycles", searchCycles);

        final Map<String, String> services = searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));
        model.addAttribute("services", services);

        final Map<String, String> queues = searchApiInterface.queues(new SearchQueueNameRequest()).stream().collect(Collectors.toMap(SearchQueueNameResponse::getNumber, SearchQueueNameResponse::getHanName));
        model.addAttribute("queues", queues);

        return "admin/stat/inbound/inbound/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(StatInboundSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatInboundTimeResponse<?>> list = apiInterface.list(search);
        final StatInboundResponse total = apiInterface.total(search);
        new InboundStatExcel(list, total).generator(response, "인바운드통계");
    }
}
