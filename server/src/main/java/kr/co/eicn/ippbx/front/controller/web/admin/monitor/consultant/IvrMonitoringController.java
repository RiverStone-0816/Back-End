package kr.co.eicn.ippbx.front.controller.web.admin.monitor.consultant;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.monitor.consultant.IvrMonitoringApiInterface;
import kr.co.eicn.ippbx.front.service.api.service.ContextApiInterface;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.server.model.dto.eicn.ContextInfoResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitorIvrResponse;
import kr.co.eicn.ippbx.server.model.search.search.SearchServiceRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/monitor/consultant/ivr")
public class IvrMonitoringController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IvrMonitoringController.class);

    private final IvrMonitoringApiInterface apiInterface;
    private final SearchApiInterface searchApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final ContextApiInterface contextApiInterface;

    @GetMapping("")
    public String page(Model model, @RequestParam(required = false) String serviceNumber) throws IOException, ResultFailException {

        final Map<String, String> services = searchApiInterface.services(new SearchServiceRequest()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));
        model.addAttribute("services", services);

        final Map<Integer, String> statusCodes = companyApiInterface.getMemberStatusCodes().stream().collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("statusCodes", statusCodes);

        final Map<String, String> contexts = contextApiInterface.list().stream().collect(Collectors.toMap(ContextInfoResponse::getContext, ContextInfoResponse::getName));
        model.addAttribute("contexts",  contexts);

        MonitorIvrResponse response = null;
        if (StringUtils.isNotEmpty(serviceNumber)) {
             response = apiInterface.getIvrList(serviceNumber);
        }

        model.addAttribute("serviceNumber", serviceNumber);
        model.addAttribute("response", response);
        return "admin/monitor/consultant/ivr/ground";
    }
}
