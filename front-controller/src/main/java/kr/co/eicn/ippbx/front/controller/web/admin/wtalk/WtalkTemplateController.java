package kr.co.eicn.ippbx.front.controller.web.admin.wtalk;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.wtalk.WtalkTemplateApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkTemplateSummaryResponse;
import kr.co.eicn.ippbx.model.enums.TalkTemplate;
import kr.co.eicn.ippbx.model.search.TemplateSearchRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.SneakyThrows;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/wtalk/template")
public class WtalkTemplateController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkTemplateController.class);

    @Autowired
    private WtalkTemplateApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;

    @SneakyThrows
    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TemplateSearchRequest search) {
        final Pagination<WtalkTemplateSummaryResponse> pagination = apiInterface.getPagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> templateTypes = FormUtils.optionsOfCode(TalkTemplate.class);
        model.addAttribute("templateTypes", templateTypes);


        final Map<Integer, String> metaTypeList = organizationService.metaTypeMap();
        model.addAttribute("metaTypeList", metaTypeList);

        model.addAttribute("types", FormUtils.options(TalkTemplate.class));

        Map<String, String> metaTypeListG = new HashMap<>();
        Map<String, String> metaTypeListC = new HashMap<>();
        Map<String, String> metaTypeListP = new HashMap<>();

        final List<WtalkTemplateSummaryResponse> list = apiInterface.list(search);

        final List<String> listP = list.stream().filter(e->e.getType().equals("P")).map(WtalkTemplateSummaryResponse::getTypeData).distinct().collect(Collectors.toList());
        final List<String> listG = list.stream().filter(e->e.getType().equals("G")).map(WtalkTemplateSummaryResponse::getTypeData).distinct().collect(Collectors.toList());
        final List<String> listC = list.stream().filter(e->e.getType().equals("C")).map(WtalkTemplateSummaryResponse::getTypeData).distinct().collect(Collectors.toList());

        for(int i=0;i<listP.size();i++){
            for(WtalkTemplateSummaryResponse response : list){
                if(response.getTypeData().equals(listP.get(i))) {
                    metaTypeListP.put(listP.get(i), response.getTypeDataName());
                    break;
                }
            }
        }
        for(int i=0;i<listG.size();i++){
            for(WtalkTemplateSummaryResponse response : list){
                if(response.getTypeData().equals(listG.get(i))) {
                    metaTypeListG.put(listG.get(i), response.getTypeDataName());
                    break;
                }
            }
        }
        for(int i=0;i<listC.size();i++){
            for(WtalkTemplateSummaryResponse response : list){
                if(response.getTypeData().equals(listC.get(i))) {
                    metaTypeListC.put(listC.get(i), response.getTypeDataName());
                    break;
                }
            }
        }

        final Map<Integer,String> m = apiInterface.list(search).stream().collect(Collectors.toMap(WtalkTemplateSummaryResponse::getSeq,WtalkTemplateSummaryResponse::getTypeData));

        model.addAttribute("metaTypeListG", metaTypeListG);
        model.addAttribute("metaTypeListC", metaTypeListC);
        model.addAttribute("metaTypeListP", metaTypeListP);

        final Map<String,Map<String, String>> metaTypeLists = new HashMap<>();

        metaTypeLists.put("G",metaTypeListG);
        metaTypeLists.put("C",metaTypeListC);
        metaTypeLists.put("P",metaTypeListP);

        model.addAttribute("metaTypeLists", metaTypeLists);

        final HashSet<String> writeUserId = new HashSet<>();
        pagination.getRows().forEach(e->writeUserId.add(e.getWriteUserName()));
        model.addAttribute("writeUserId", writeUserId);

        final ArrayList<String> mentName = new ArrayList<>();
        pagination.getRows().forEach(e->mentName.add(e.getMentName()));
        model.addAttribute("mentName", mentName);

        return "admin/wtalk/template/ground";
    }

    @SneakyThrows
    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") WtalkTemplateApiInterface.TemplateForm form) {
        final Map<String, String> templateTypes = FormUtils.optionsOfCode(TalkTemplate.class);
        model.addAttribute("templateTypes", templateTypes);

        return "admin/wtalk/template/modal";
    }

    @SneakyThrows
    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") WtalkTemplateApiInterface.TemplateForm form) {
        final WtalkTemplateSummaryResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);
        form.setTypeMent(entity.getTypeMent());

        if (entity.getType().equals(TalkTemplate.GROUP.getCode()) && StringUtils.isNotEmpty(form.getTypeData()))
            model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(form.getTypeData()));

        return modal(model, form);
    }
}
