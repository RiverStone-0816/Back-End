package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsResultGroupApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.PDSResultGroupStrategy;
import kr.co.eicn.ippbx.model.form.PDSResultGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PDSResultGroupSearchRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/result-group")
public class PdsResultGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsResultGroupController.class);

    @Autowired
    private PdsResultGroupApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CommonTypeApiInterface commonTypeApiInterface;
    @Autowired
    private CompanyApiInterface companyApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSResultGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PDSResultGroupSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/outbound/pds/result-group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") PDSResultGroupFormRequest form) throws IOException, ResultFailException {

        final Map<String, String> hosts = apiInterface.addServerLists().stream().collect(Collectors.toMap(SummaryCompanyServerResponse::getHost, SummaryCompanyServerResponse::getName));
        model.addAttribute("hosts", hosts);

        final Map<String, String> strategyOptions = FormUtils.optionsOfCode(PDSResultGroupStrategy.class);
        model.addAttribute("strategyOptions", strategyOptions);

        final Map<String, String> contexts = apiInterface.context().stream().collect(Collectors.toMap(SummaryContextInfoResponse::getContext, SummaryContextInfoResponse::getName));
        model.addAttribute("contexts", contexts);

        final List<SummaryPersonResponse> addOnPersons = apiInterface.addOnPersons(null);
        model.addAttribute("addOnPersons", addOnPersons);

        return "admin/outbound/pds/result-group/modal";
    }

    @GetMapping("{name}/modal")
    public String modal(Model model, @PathVariable String name, @ModelAttribute("form") PDSResultGroupFormRequest form) throws IOException, ResultFailException {
        final PDSResultGroupDetailResponse entity = apiInterface.get(name);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final Map<String, String> hosts = apiInterface.addServerLists().stream().collect(Collectors.toMap(SummaryCompanyServerResponse::getHost, SummaryCompanyServerResponse::getName));
        model.addAttribute("hosts", hosts);

        final Map<String, String> strategyOptions = FormUtils.optionsOfCode(PDSResultGroupStrategy.class);
        model.addAttribute("strategyOptions", strategyOptions);


        final Map<String, String> contexts = apiInterface.context().stream().collect(Collectors.toMap(SummaryContextInfoResponse::getContext, SummaryContextInfoResponse::getName));
        model.addAttribute("contexts", contexts);
        List<String> person = new ArrayList<>();
        if(entity.getAddPersons() != null) {
            person = entity.getAddPersons().stream().map(SummaryQueuePersonResponse::getUserId).collect(Collectors.toList());
        }
        List<String> finalPerson = person;
        final List<SummaryPersonResponse> addOnPersons = apiInterface.addOnPersons(name).stream().filter(e -> !finalPerson.contains(e.getUserId())).collect(Collectors.toList());
        model.addAttribute("addOnPersons", addOnPersons);

        return "admin/outbound/pds/result-group/modal";
    }
}
