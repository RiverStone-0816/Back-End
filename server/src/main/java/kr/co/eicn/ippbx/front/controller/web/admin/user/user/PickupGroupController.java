package kr.co.eicn.ippbx.front.controller.web.admin.user.user;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.PickupGroupApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.PickUpGroupDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PickUpGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPersonResponse;
import kr.co.eicn.ippbx.server.model.form.PickUpGroupFormRequest;
import kr.co.eicn.ippbx.server.model.form.PickUpGroupFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.search.PickUpGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static java.util.stream.Collectors.toSet;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/user/user/pickup-group")
public class PickupGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PickupGroupController.class);

    @Autowired
    private PickupGroupApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CompanyApiInterface companyApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PickUpGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PickUpGroupSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        return "admin/user/user/pickup-group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") PickUpGroupFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("pbxServers", companyApiInterface.getPBXServers());

        return "admin/user/user/pickup-group/modal";
    }

    @GetMapping("{groupCode}/modal")
    public String modal(Model model, @PathVariable Integer groupCode, @ModelAttribute("form") PickUpGroupFormUpdateRequest form) throws IOException, ResultFailException {
        final PickUpGroupDetailResponse entity = apiInterface.get(groupCode);
        ReflectionUtils.copy(form, entity);

        form.setPeers(entity.getAddPersons().stream().map(SummaryPersonResponse::getPeer).collect(toSet()));

        model.addAttribute("entity", entity);

        return "admin/user/user/pickup-group/modal-user-set";
    }
}
