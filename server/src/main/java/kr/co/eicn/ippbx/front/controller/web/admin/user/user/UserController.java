package kr.co.eicn.ippbx.front.controller.web.admin.user.user;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.MenuApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.configdb.UserMenuCompanyResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyLicenceEntity;
import kr.co.eicn.ippbx.server.model.enums.DataSearchAuthorityType;
import kr.co.eicn.ippbx.server.model.enums.GroupLevelAuth;
import kr.co.eicn.ippbx.server.model.enums.IdType;
import kr.co.eicn.ippbx.server.model.enums.RecordingAuthorityType;
import kr.co.eicn.ippbx.server.model.form.MenuFormRequest;
import kr.co.eicn.ippbx.server.model.form.PersonFormRequest;
import kr.co.eicn.ippbx.server.model.form.PersonPasswordUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.UserMenuSequenceUpdateRequest;
import kr.co.eicn.ippbx.server.model.search.PersonSearchRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/user/user/user")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserApiInterface apiInterface;
    private final OrganizationService organizationService;
    private final CompanyApiInterface companyApiInterface;
    private final MenuApiInterface menuApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PersonSearchRequest search) throws IOException, ResultFailException {
        final Pagination<PersonSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        final CompanyLicenceEntity license = companyApiInterface.getLicense();
        model.addAttribute("license", license);
        final String services = companyApiInterface.getServices();
        model.addAttribute("services", services);

        return "admin/user/user/user/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") PersonFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("idTypes", FormUtils.optionsOfCode(IdType.SUPER_ADMIN, IdType.ADMIN, IdType.USER));
        model.addAttribute("recordingAuthorityTypes", FormUtils.optionsOfCode(RecordingAuthorityType.class));
        model.addAttribute("dataSearchAuthorityTypes", FormUtils.optionsOfCode(DataSearchAuthorityType.class));

        final List<String> extensions = apiInterface.extensions().stream().map(SummaryPhoneInfoResponse::getExtension).collect(Collectors.toList());
        model.addAttribute("extensions", extensions);
        final CompanyLicenceEntity license = companyApiInterface.getLicense();
        model.addAttribute("license", license);
        final String services = companyApiInterface.getServices();
        model.addAttribute("services", services);


        return "admin/user/user/user/modal";
    }

    @GetMapping("{id}/modal")
    public String modal(Model model, @PathVariable String id, @ModelAttribute("form") PersonFormRequest form) throws IOException, ResultFailException {
        final PersonDetailResponse entity = apiInterface.get(id);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        model.addAttribute("idTypes", FormUtils.optionsOfCode(IdType.SUPER_ADMIN, IdType.ADMIN, IdType.USER));
        model.addAttribute("recordingAuthorityTypes", FormUtils.optionsOfCode(RecordingAuthorityType.class));
        model.addAttribute("dataSearchAuthorityTypes", FormUtils.optionsOfCode(DataSearchAuthorityType.class));

        final List<String> extensions = apiInterface.extensions(entity.getExtension()).stream().map(SummaryPhoneInfoResponse::getExtension).collect(Collectors.toList());
        model.addAttribute("extensions", extensions);

        if (!extensions.contains(entity.getExtension()))
            extensions.add(0, entity.getExtension());

        final CompanyLicenceEntity license = companyApiInterface.getLicense();
        model.addAttribute("license", license);
        final String services = companyApiInterface.getServices();
        model.addAttribute("services", services);


        return "admin/user/user/user/modal";
    }

    @GetMapping("{id}/modal-update-password")
    public String modal(Model model, @PathVariable String id, @ModelAttribute("form") PersonPasswordUpdateRequest form) throws IOException, ResultFailException {
        model.addAttribute("id", id);
        return "admin/user/user/user/modal-update-password";
    }

    @GetMapping("{id}/modal-menu")
    public String modalMenu(Model model, @PathVariable String id) throws IOException, ResultFailException {
        final List<UserMenuCompanyResponse> menus = menuApiInterface.getUserMenus(id);
        model.addAttribute("menus", menus);

        final PersonDetailResponse entity = apiInterface.get(id);
        model.addAttribute("entity", entity);

        return "admin/user/user/user/modal-menu";
    }

    @GetMapping("{id}/modal-menu/{menuCode}")
    public String modalMenu(Model model, @PathVariable String id, @PathVariable String menuCode, @ModelAttribute("form") MenuFormRequest form) throws IOException, ResultFailException {
        final UserMenuCompanyResponse menu = menuApiInterface.getChildrenMenuByParentMenu(id, menuCode);
        model.addAttribute("menu", menu);
        model.addAttribute("userId", id);

        form.setMenuName(menu.getMenuName());
        form.setView(Objects.equals(menu.getViewYn(), "Y"));
        form.setGroupCode(menu.getGroupCode());
        form.setGroupLevelAuth(menu.getGroupLevelAuthYn());

        model.addAttribute("groupLevelAuths", FormUtils.optionsOfCode(GroupLevelAuth.class));

        if (StringUtils.isNotEmpty(menu.getGroupCode()))
            model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(menu.getGroupCode()));

        return "admin/user/user/user/modal-menu-attribute";
    }

    @GetMapping("{id}/modal-menu-sequence")
    public String modalMenuSequence(Model model, @PathVariable String id, @ModelAttribute("form") UserMenuSequenceUpdateRequest form) throws IOException, ResultFailException {
        final UserMenuCompanyResponse menu = new UserMenuCompanyResponse();
        menu.setChildren(menuApiInterface.getUserMenus(id));
        model.addAttribute("menu", menu);
        model.addAttribute("userId", id);

        return "admin/user/user/user/modal-menu-sequence";
    }

    @GetMapping("{id}/modal-menu-sequence/{menuCode}")
    public String modalMenuSequence(Model model, @PathVariable String id, @PathVariable String menuCode, @ModelAttribute("form") UserMenuSequenceUpdateRequest form) throws IOException, ResultFailException {
        final UserMenuCompanyResponse menu = menuApiInterface.getChildrenMenuByParentMenu(id, menuCode);
        model.addAttribute("menu", menu);
        model.addAttribute("userId", id);

        return "admin/user/user/user/modal-menu-sequence";
    }
}
