package kr.co.eicn.ippbx.front.controller.web.admin.service.etc;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.service.etc.MonitApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.dto.eicn.MonitControlResponse;
import kr.co.eicn.ippbx.model.search.MonitControlSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("admin/service/etc/monit")
public class MonitController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MonitController.class);

    private final MonitApiInterface apiInterface;
    private final OrganizationService organizationService;
    private final CompanyApiInterface companyApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") MonitControlSearchRequest search) throws IOException, ResultFailException {
        final List<MonitControlResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        final Map<Integer, String> statusCodes = companyApiInterface.getMemberStatusCodes().stream().filter(e -> e.getStatusNumber() != 1).collect(Collectors.toMap(CmpMemberStatusCode::getStatusNumber, CmpMemberStatusCode::getStatusName));
        model.addAttribute("statusCodes", statusCodes);

        return "admin/service/etc/monit/ground";
    }

}
