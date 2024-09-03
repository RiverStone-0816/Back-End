package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.front.service.excel.example.PdsCustomUploadExampleExcel;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.enums.PDSGroupSpeedMultiple;
import kr.co.eicn.ippbx.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PDSGroupSearchRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private PdsGroupApiInterface   apiInterface;
    @Autowired
    private OrganizationService    organizationService;
    @Autowired
    private CommonTypeApiInterface commonTypeApiInterface;
    @Autowired
    private CompanyApiInterface    companyApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PDSGroupSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("pdsTypes", apiInterface.addCommonTypeLists("PDS").stream().collect(Collectors.toMap(SummaryCommonTypeResponse::getSeq, SummaryCommonTypeResponse::getName)));

        return "admin/outbound/pds/group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") PDSGroupFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("hosts", apiInterface.addServerLists().stream().collect(Collectors.toMap(SummaryCompanyServerResponse::getHost, SummaryCompanyServerResponse::getName)));
        model.addAttribute("pdsTypes", apiInterface.addCommonTypeLists("PDS").stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryCommonTypeResponse::getName)));

        model.addAttribute("ivrs", apiInterface.addPDSIvrLists().stream().collect(Collectors.toMap(SummaryIvrTreeResponse::getCode, SummaryIvrTreeResponse::getName)));
        model.addAttribute("queues", apiInterface.addPDSQueueNameLists());
        if (g.getUsingServices().contains("RSH"))
            model.addAttribute("researchList", apiInterface.addResearchLists().stream().collect(Collectors.toMap(SummaryResearchListResponse::getResearchId, SummaryResearchListResponse::getResearchName)));

        model.addAttribute("commonFields", apiInterface.addCommonFieldLists());
        model.addAttribute("numbers", apiInterface.addNumberLists().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, SummaryNumber070Response::getNumber)));
        model.addAttribute("pdsGroupSpeedOptions", FormUtils.optionsOfCode(PDSGroupSpeedMultiple.class));
        model.addAttribute("rsTypes", apiInterface.addCommonTypeLists("RS").stream().collect(Collectors.toMap(e -> e.getSeq().toString(), SummaryCommonTypeResponse::getName)));

        return "admin/outbound/pds/group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") PDSGroupFormRequest form) throws IOException, ResultFailException {
        final PDSGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        return modal(model, form);
    }

    @GetMapping("{seq}/modal-execution-request")
    public String modalExecutionRequest(Model model, @PathVariable Integer seq, @ModelAttribute("form") PDSExecuteFormRequest form) throws IOException, ResultFailException {
        final PDSGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("hosts", apiInterface.addServerLists().stream().collect(Collectors.toMap(SummaryCompanyServerResponse::getHost, SummaryCompanyServerResponse::getName)));
        model.addAttribute("commonFields", apiInterface.addCommonFieldLists().stream().filter(e -> e.getType().equals(entity.getPdsType())).collect(Collectors.toList()));
        model.addAttribute("numbers", apiInterface.addNumberLists().stream().collect(Collectors.toMap(SummaryNumber070Response::getNumber, SummaryNumber070Response::getNumber)));
        model.addAttribute("pdsGroupSpeedOptions", FormUtils.optionsOfCode(PDSGroupSpeedMultiple.class));

        return "admin/outbound/pds/group/modal-execution-request";
    }

    @GetMapping("{seq}/modal-upload")
    public String modalUpload(Model model, @PathVariable Integer seq, @ModelAttribute("form") FileForm form) throws IOException, ResultFailException {
        final PDSGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));
        model.addAttribute("commonTypeEntity", commonTypeApiInterface.get(entity.getPdsType()));
        model.addAttribute("company", companyApiInterface.getInfo());

        return "admin/outbound/pds/group/modal-upload";
    }

    @GetMapping("{seq}/_excel/example")
    public void downloadExcel(@PathVariable Integer seq, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final PDSGroupDetailResponse group = apiInterface.get(seq);
        final CommonTypeEntity pdsType = commonTypeApiInterface.get(group.getPdsType());

        new PdsCustomUploadExampleExcel(group, pdsType).generator(response, "PDS그룹_고객정보_업로드_예시");
    }
}
