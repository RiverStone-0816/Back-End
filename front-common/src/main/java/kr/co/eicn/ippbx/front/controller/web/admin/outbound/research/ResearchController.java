package kr.co.eicn.ippbx.front.controller.web.admin.outbound.research;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchItemApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchListResponse;
import kr.co.eicn.ippbx.model.form.ResearchListFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchItemSearchRequest;
import kr.co.eicn.ippbx.model.search.ResearchListSearchRequest;
import lombok.AllArgsConstructor;
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

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/research/research")
public class ResearchController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ResearchController.class);

    private final ResearchApiInterface apiInterface;
    private final ResearchItemApiInterface researchItemApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") ResearchListSearchRequest search) throws IOException, ResultFailException {
        final Pagination<ResearchListResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/outbound/research/research/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ResearchListFormRequest form) throws IOException, ResultFailException {
        return "admin/outbound/research/research/modal";
    }

    @GetMapping("{researchId}/modal")
    public String modal(Model model, @ModelAttribute("form") ResearchListFormRequest form, @PathVariable Integer researchId) throws IOException, ResultFailException {
        final ResearchListResponse entity = apiInterface.get(researchId);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        return "admin/outbound/research/research/modal";
    }

    @GetMapping("{researchId}/modal-scenario")
    public String modalScenario(Model model, @PathVariable Integer researchId) throws IOException, ResultFailException {
        final ResearchListResponse entity = apiInterface.get(researchId);
        model.addAttribute("entity", entity);

        if (Objects.equals(entity.getHaveTree(), "Y"))
            model.addAttribute("scenario", apiInterface.getScenario(researchId).getScenario());

        final ResearchItemSearchRequest search = new ResearchItemSearchRequest();
        search.setLimit(1000);
        search.setMappingNumber((byte) 0);
        final List<ResearchItemResponse> items = researchItemApiInterface.pagination(search).getRows();
        model.addAttribute("items", items);

        return "admin/outbound/research/research/modal-scenario";
    }
}
