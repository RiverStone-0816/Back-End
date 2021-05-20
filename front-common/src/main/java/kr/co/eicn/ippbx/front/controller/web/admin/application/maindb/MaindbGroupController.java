package kr.co.eicn.ippbx.front.controller.web.admin.application.maindb;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbGroupApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.model.dto.eicn.CommonTypeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.model.enums.DupKeyKind;
import kr.co.eicn.ippbx.model.form.MaindbGroupFormRequest;
import kr.co.eicn.ippbx.model.form.MaindbGroupUpdateRequest;
import kr.co.eicn.ippbx.model.search.MaindbGroupSearchRequest;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/application/maindb/group")
public class MaindbGroupController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MaindbGroupController.class);

    @Autowired
    private MaindbGroupApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CompanyApiInterface companyApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") MaindbGroupSearchRequest search) throws IOException, ResultFailException {
        final Pagination<MaindbGroupSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<Integer, String> maindbTypes = apiInterface.maindbType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName));
        model.addAttribute("maindbTypes", maindbTypes);

        return "admin/application/maindb/group/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") MaindbGroupFormRequest form) throws IOException, ResultFailException {
        final Map<Integer, String> maindbTypes = apiInterface.maindbType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName));
        model.addAttribute("maindbTypes", maindbTypes);

        final List<CommonField> maindbFields = apiInterface.maindbField();
        model.addAttribute("maindbFields", maindbFields);

        final Map<Integer, String> resultTypes = apiInterface.resultType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName));
        model.addAttribute("resultTypes", resultTypes);

        final LinkedHashMap<String, String> dupKeyKTypes = FormUtils.optionsOfCode(DupKeyKind.class);
        model.addAttribute("dupKeyKTypes", dupKeyKTypes);

        return "admin/application/maindb/group/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") MaindbGroupUpdateRequest form) throws IOException, ResultFailException {
        final MaindbGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        final Map<Integer, String> maindbTypes = apiInterface.maindbType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName));
        model.addAttribute("maindbTypes", maindbTypes);

        final Map<Integer, String> resultTypes = apiInterface.resultType().stream().collect(Collectors.toMap(CommonTypeResponse::getSeq, CommonTypeResponse::getName));
        model.addAttribute("resultTypes", resultTypes);

        if (!maindbTypes.containsKey(entity.getMaindbType()))
            maindbTypes.put(entity.getMaindbType(), entity.getMaindbName());

        if (!resultTypes.containsKey(entity.getResultType()))
            resultTypes.put(entity.getResultType(), entity.getResultName());

        return "admin/application/maindb/group/modal";
    }

    @GetMapping("{seq}/modal-upload")
    public String modalUpload(Model model, @PathVariable Integer seq, @ModelAttribute("form") FileForm form) throws IOException, ResultFailException {
        final MaindbGroupDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        final CompanyEntity company = companyApiInterface.getInfo();
        model.addAttribute("company", company);

        return "admin/application/maindb/group/modal-upload";
    }
}
