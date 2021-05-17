package kr.co.eicn.ippbx.front.controller.web.admin.user.tel;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.tel.RandomRidApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.RandomCidResponse;
import kr.co.eicn.ippbx.server.model.form.RandomCidFormRequest;
import kr.co.eicn.ippbx.server.model.search.RandomCidSearchRequest;
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

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/user/tel/random-rid")
public class RandomRidController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RandomRidController.class);

    @Autowired
    private RandomRidApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") RandomCidSearchRequest search) throws IOException, ResultFailException {
        final Pagination<RandomCidResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));

        return "admin/user/tel/random-rid/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") RandomCidFormRequest form) throws IOException, ResultFailException {
        final List<Byte> shortNumList = new ArrayList<>();
        for (byte i = 1; i <= 9; i++)
            shortNumList.add(i);

        model.addAttribute("shortNumList", shortNumList);

        return "admin/user/tel/random-rid/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") RandomCidFormRequest form) throws IOException, ResultFailException {
        final RandomCidResponse entity = apiInterface.get(seq);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(entity.getGroupCode()));

        return modal(model, form);
    }
}
