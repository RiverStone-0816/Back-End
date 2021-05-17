package kr.co.eicn.ippbx.front.controller.web.admin.user;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.OrganizationTree;
import kr.co.eicn.ippbx.front.model.search.OrganizationPanCondition;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.OrganizationApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.CompanyTreeLevelNameResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationPersonSummaryResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.Organization;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/user/organization")
public class OrganizationController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<CompanyTreeLevelNameResponse> metaTypes = apiInterface.listMetaType();
        if (metaTypes.size() == 0)
            throw new IllegalStateException("메타데이터가 존재하지 않습니다.");

        model.addAttribute("metaTypes", metaTypes);
        return "admin/user/organization/ground";
    }

    @GetMapping("editable-pan")
    public String pan(Model model, @ModelAttribute("condition") OrganizationPanCondition condition) throws IOException, ResultFailException {
        final List<CompanyTreeLevelNameResponse> metaTypes = apiInterface.listMetaType();
        if (metaTypes.size() == 0)
            throw new IllegalStateException("메타데이터가 존재하지 않습니다.");

        final List<Organization> list = apiInterface.getAllOrganizationPersons();
        final OrganizationTree root = getOrganizationTree(list, /*condition.getKeyword()*/ null);

        model.addAttribute("metaTypes", metaTypes);
        model.addAttribute("maxLevel", metaTypes.get(metaTypes.size() - 1).getGroupLevel());
        model.addAttribute("root", root);
        return "admin/user/organization/editable-pan";
    }

    @GetMapping("meta-type/modal")
    public String modalMetaType(Model model) throws IOException, ResultFailException {
        final List<CompanyTreeLevelNameResponse> metaTypes = apiInterface.listMetaType();
        model.addAttribute("metaTypes", metaTypes);
        return "admin/user/organization/modal-meta-type";
    }

    @GetMapping("modal-select-organization")
    public String pan(Model model) throws IOException, ResultFailException {
        final List<Organization> list = apiInterface.getAllOrganizationPersons();
        final OrganizationTree root = getOrganizationTree(list, null);

        model.addAttribute("root", root);
        return "admin/user/organization/modal-select-organization";
    }

    @GetMapping("{seq}/summary")
    public String summary(Model model, @PathVariable Integer seq) throws IOException, ResultFailException {
        final List<CompanyTreeLevelNameResponse> metaTypes = apiInterface.listMetaType();
        model.addAttribute("metaTypes", metaTypes);
        final OrganizationPersonSummaryResponse summary = apiInterface.getOrganizationPersonSummary(seq);
        model.addAttribute("summary", summary);
        final Organization element = apiInterface.getOrganizationPerson(seq);
        model.addAttribute("element", element);
        return "admin/user/organization/element-summary";
    }

    private OrganizationTree getOrganizationTree(List<Organization> list, String keyword) {
        final OrganizationTree root = new OrganizationTree();
        setDescendant(root, list, keyword);
        return root;
    }

    private boolean setDescendant(OrganizationTree parent, List<Organization> list, String keyword) {
        list.stream().filter(e -> Objects.equals(e.getParentGroupCode(), parent.getGroupCode()))
                .map(OrganizationTree::new)
                .forEach(e -> parent.getChildren().add(e));

        boolean hasKeyword = false;
        final List<OrganizationTree> removing = new ArrayList<>();

        for (OrganizationTree child : parent.getChildren()) {
            if (setDescendant(child, list, keyword))
                hasKeyword = true;
            else
                removing.add(child);
        }

        parent.getChildren().removeAll(removing);

        if (StringUtils.isEmpty(parent.getGroupName()) || StringUtils.isEmpty(keyword) || parent.getGroupName().contains(keyword))
            hasKeyword = true;

        if (StringUtils.isNotEmpty(keyword) && parent.getPersons() != null && parent.getPersons().stream().anyMatch(e -> e.getIdName().contains(keyword) || e.getId().contains(keyword)))
            hasKeyword = true;

        return hasKeyword;
    }

}
