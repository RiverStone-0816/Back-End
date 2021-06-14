package kr.co.eicn.ippbx.front.controller.web.admin.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.route.CsRouteApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.ConsultantStatApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.front.service.excel.ConsultantStatExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserResponse;
import kr.co.eicn.ippbx.model.enums.SearchCycle;
import kr.co.eicn.ippbx.model.search.PersonSearchRequest;
import kr.co.eicn.ippbx.model.search.StatUserSearchRequest;
import kr.co.eicn.ippbx.model.search.search.SearchServiceRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("admin/stat/consultant/consultant")
public class ConsultantStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConsultantStatController.class);

    private final ConsultantStatApiInterface consultantStatApiInterface;
    private final SearchApiInterface searchApiInterface;
    private final CsRouteApiInterface csRouteApiInterface;
    private final UserApiInterface userApiInterface;
    private final OrganizationService organizationService;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") StatUserSearchRequest search) throws IOException, ResultFailException {
        final List<StatUserResponse<?>> list = consultantStatApiInterface.list(search);
        model.addAttribute("list", list);

        final StatUserResponse.UserStat total = consultantStatApiInterface.getTotal(search);
        model.addAttribute("total", total);

        final Map<String, String> searchCycles = FormUtils.options(false, SearchCycle.class);
        model.addAttribute("searchCycles", searchCycles);

        final Map<String, String> services = searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));
        model.addAttribute("services", services);

        final Map<String, String> queues = csRouteApiInterface.queue().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName));
        model.addAttribute("queues", queues);

        final PersonSearchRequest personSearchRequest = new PersonSearchRequest();
        personSearchRequest.setLimit(1000);
        model.addAttribute("persons", userApiInterface.pagination(personSearchRequest).getRows().stream().collect(Collectors.toMap(PersonSummaryResponse::getId, PersonSummaryResponse::getIdName)));

        if (StringUtils.isNotEmpty(search.getGroupCode()))
            model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        return "admin/stat/consultant/consultant/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(StatUserSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatUserResponse<?>> list = consultantStatApiInterface.list(search);
        final StatUserResponse.UserStat total = consultantStatApiInterface.getTotal(search);

        new ConsultantStatExcel(list, total).generator(response, "상담원(개인별)실적통계");
    }
}
