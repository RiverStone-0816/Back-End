package kr.co.eicn.ippbx.front.controller.web.admin.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.route.CsRouteApiInterface;
import kr.co.eicn.ippbx.front.service.api.stat.ConsultantStatApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.front.service.excel.ConsultantCallStatExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
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
@RequestMapping("admin/stat/consultant/call")
public class ConsultantCallStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConsultantCallStatController.class);

    private final ConsultantStatApiInterface apiInterface;
    private final SearchApiInterface searchApiInterface;
    private final CsRouteApiInterface csRouteApiInterface;
    private final UserApiInterface userApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final OrganizationService organizationService;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") StatUserSearchRequest search) throws IOException, ResultFailException {
        final List<StatUserResponse<?>> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final StatUserResponse.UserStat total = apiInterface.getTotal(search);
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

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("memberStatuses", memberStatuses);

        return "admin/stat/consultant/call/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(StatUserSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatUserResponse<?>> list = apiInterface.list(search);
        final StatUserResponse.UserStat total = apiInterface.getTotal(search);

        final Map<Integer, String> memberStatuses = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));

        new ConsultantCallStatExcel(list, total, memberStatuses).generator(response, "상담원별콜실적");
    }
}
