package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PDSGroupSearchRequest;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/group")
public class PdsGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsGroupController.class);

    @Autowired
    private PdsGroupApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CommonTypeApiInterface commonTypeApiInterface;
    @Autowired
    private CompanyApiInterface companyApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PDSGroupSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<Integer, String> pdsTypes = apiInterface.addCommonTypeLists("PDS").stream().collect(Collectors.toMap(SummaryCommonTypeResponse::getSeq, SummaryCommonTypeResponse::getName));
        model.addAttribute("pdsTypes", pdsTypes);

        return "admin/outbound/pds/group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") PDSGroupFormRequest form) throws IOException, ResultFailException {

        final Map<String, String> hosts = apiInterface.addServerLists().stream().collect(Collectors.toMap(SummaryCompanyServerResponse::getHost, SummaryCompanyServerResponse::getName));
        model.addAttribute("hosts", hosts);

        final Map<String, String> pdsTypes = apiInterface.addCommonTypeLists("PDS").stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryCommonTypeResponse::getName));
        model.addAttribute("pdsTypes", pdsTypes);

        final Map<String, String> numbers = apiInterface.addNumberLists().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, SummaryNumber070Response::getNumber));
        model.addAttribute("numbers", numbers);

        final List<SummaryPDSQueueNameResponse> queues = apiInterface.addPDSQueueNameLists();
        model.addAttribute("queues", queues);

        final Map<Integer, String> ivrs = apiInterface.addPDSIvrLists().stream().collect(Collectors.toMap(SummaryIvrTreeResponse::getCode, SummaryIvrTreeResponse::getName));
        model.addAttribute("ivrs", ivrs);

        final Map<String, String> rsTypes = apiInterface.addCommonTypeLists("RS").stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryCommonTypeResponse::getName));
        model.addAttribute("rsTypes", rsTypes);

        final List<SummaryCommonFieldResponse> commonFieldResponses = apiInterface.addCommonFieldLists();
        model.addAttribute("commonFields", commonFieldResponses);

        final Map<Integer, String> researchList = apiInterface.addResearchLists().stream().collect(Collectors.toMap(SummaryResearchListResponse::getResearchId, SummaryResearchListResponse::getResearchName));
        model.addAttribute("researchList", researchList);

        return "admin/outbound/pds/group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") PDSGroupFormRequest form) throws IOException, ResultFailException {
        final PDSGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        final Map<String, String> hosts = apiInterface.addServerLists().stream().collect(Collectors.toMap(SummaryCompanyServerResponse::getHost, SummaryCompanyServerResponse::getName));
        model.addAttribute("hosts", hosts);

        final Map<String, String> pdsTypes = apiInterface.addCommonTypeLists("PDS").stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryCommonTypeResponse::getName));
        model.addAttribute("pdsTypes", pdsTypes);

        final Map<String, String> numbers = apiInterface.addNumberLists().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, SummaryNumber070Response::getNumber));
        model.addAttribute("numbers", numbers);

        final List<SummaryPDSQueueNameResponse> queues = apiInterface.addPDSQueueNameLists();
        model.addAttribute("queues", queues);

        final Map<Integer, String> ivrs = apiInterface.addPDSIvrLists().stream().collect(Collectors.toMap(SummaryIvrTreeResponse::getCode, SummaryIvrTreeResponse::getName));
        model.addAttribute("ivrs", ivrs);

        final Map<String, String> rsTypes = apiInterface.addCommonTypeLists("RS").stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryCommonTypeResponse::getName));
        model.addAttribute("rsTypes", rsTypes);

        final List<SummaryCommonFieldResponse> commonFieldResponses = apiInterface.addCommonFieldLists();
        model.addAttribute("commonFields", commonFieldResponses);

        final Map<Integer, String> researchList = apiInterface.addResearchLists().stream().collect(Collectors.toMap(SummaryResearchListResponse::getResearchId, SummaryResearchListResponse::getResearchName));
        model.addAttribute("researchList", researchList);

        return "admin/outbound/pds/group/modal";
    }

    @GetMapping("{seq}/modal-execution-request")
    public String modalExecutionRequest(Model model, @PathVariable Integer seq, @ModelAttribute("form") PDSExecuteFormRequest form) throws IOException, ResultFailException {
        final PDSGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        final Map<String, String> hosts = apiInterface.addServerLists().stream().collect(Collectors.toMap(SummaryCompanyServerResponse::getHost, SummaryCompanyServerResponse::getName));
        model.addAttribute("hosts", hosts);

        final Map<String, String> numbers = apiInterface.addNumberLists().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, SummaryNumber070Response::getNumber));
        model.addAttribute("numbers", numbers);

        final Map<String, String> queues = apiInterface.addPDSQueueNameLists().stream().collect(Collectors.toMap(SummaryPDSQueueNameResponse::getName, SummaryPDSQueueNameResponse::getHanName));
        model.addAttribute("queues", queues);

        final Map<Integer, String> ivrs = apiInterface.addPDSIvrLists().stream().collect(Collectors.toMap(SummaryIvrTreeResponse::getCode, SummaryIvrTreeResponse::getName));
        model.addAttribute("ivrs", ivrs);

        final Map<String, String> rsTypes = apiInterface.addCommonTypeLists("RS").stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryCommonTypeResponse::getName));
        model.addAttribute("rsTypes", rsTypes);

        final List<SummaryCommonFieldResponse> commonFieldResponses = apiInterface.addCommonFieldLists();
        model.addAttribute("commonFields", commonFieldResponses.stream().filter(e -> e.getType().equals(entity.getPdsType())).collect(Collectors.toList()));

        final Map<Integer, String> researchList = apiInterface.addResearchLists().stream().collect(Collectors.toMap(SummaryResearchListResponse::getResearchId, SummaryResearchListResponse::getResearchName));
        model.addAttribute("researchList", researchList);

        return "admin/outbound/pds/group/modal-execution-request";
    }

    @GetMapping("{seq}/modal-upload")
    public String modalUpload(Model model, @PathVariable Integer seq, @ModelAttribute("form") FileForm form) throws IOException, ResultFailException {
        final PDSGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        final CommonTypeEntity commonTypeEntity = commonTypeApiInterface.get(entity.getPdsType());
        model.addAttribute("commonTypeEntity", commonTypeEntity);

        final CompanyEntity company = companyApiInterface.getInfo();
        model.addAttribute("company", company);

        return "admin/outbound/pds/group/modal-upload";
    }
}
