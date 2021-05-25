package kr.co.eicn.ippbx.front.controller.web.admin.outbound.research;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchItemApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.enums.ResearchItemSoundKind;
import kr.co.eicn.ippbx.model.form.ResearchItemFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchItemSearchRequest;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/outbound/research/item")
public class ResearchItemController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ResearchItemController.class);

    @Autowired
    private ResearchItemApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") ResearchItemSearchRequest search) throws IOException, ResultFailException {
        search.setMappingNumber((byte) 0);
        final Pagination<ResearchItemResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/outbound/research/item/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ResearchItemFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> soundKinds = FormUtils.optionsOfCode(ResearchItemSoundKind.class);
        model.addAttribute("soundKinds", soundKinds);

        final Map<Integer, String> sounds = apiInterface.addSounds().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        return "admin/outbound/research/item/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @ModelAttribute("form") ResearchItemFormRequest form, @PathVariable Integer seq) throws IOException, ResultFailException {
        final ResearchItemResponse entity = apiInterface.get(seq);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);
        form.setAnswerRequests(entity.getAnswers());

        final Map<String, String> soundKinds = FormUtils.optionsOfCode(ResearchItemSoundKind.class);
        model.addAttribute("soundKinds", soundKinds);

        final Map<Integer, String> sounds = apiInterface.addSounds().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", sounds);

        return "admin/outbound/research/item/modal";
    }
}
