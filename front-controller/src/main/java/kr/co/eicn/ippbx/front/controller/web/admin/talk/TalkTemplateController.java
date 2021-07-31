package kr.co.eicn.ippbx.front.controller.web.admin.talk;

import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.TalkTemplateApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.model.dto.eicn.TalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/talk/template")
public class TalkTemplateController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkTemplateController.class);

    @Autowired
    private TalkTemplateApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TemplateSearchRequest search) throws IOException, ResultFailException {
        final List<TalkTemplateSummaryResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final Map<String, String> templateTypes = FormUtils.optionsOfCode(TalkTemplate.class);
        model.addAttribute("templateTypes", templateTypes);

        final Map<Integer, String> metaTypeList = organizationService.metaTypeMap();
        model.addAttribute("metaTypeList", metaTypeList);

        model.addAttribute("types", FormUtils.options(TalkTemplate.class));

        return "admin/talk/template/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TalkTemplateFormRequest form) {
        final Map<String, String> templateTypes = FormUtils.optionsOfCode(TalkTemplate.class);
        model.addAttribute("templateTypes", templateTypes);

        return "admin/talk/template/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") TalkTemplateFormRequest form) throws IOException, ResultFailException {
        final TalkTemplateSummaryResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        if (entity.getType().equals(TalkTemplate.GROUP.getCode()) && StringUtils.isNotEmpty(form.getTypeData()))
            model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(form.getTypeData()));

        return modal(model, form);
    }
}
