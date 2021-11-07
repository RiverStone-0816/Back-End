package kr.co.eicn.ippbx.front.controller.web.admin.outbound.voc;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchItemApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.voc.VocGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.voc.VocResultApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.model.entity.customdb.VocResearchResultEntity;
import kr.co.eicn.ippbx.model.search.CustomDBVOCResultSearchRequest;
import kr.co.eicn.ippbx.model.search.ResearchItemSearchRequest;
import kr.co.eicn.ippbx.model.search.VOCGroupSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/voc/result")
public class VocResultController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(VocResultController.class);

    private final VocResultApiInterface apiInterface;
    private final VocGroupApiInterface vocGroupApiInterface;
    private final ResearchApiInterface researchApiInterface;
    private final ResearchItemApiInterface researchItemApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") CustomDBVOCResultSearchRequest search) throws IOException, ResultFailException {
        final List<VocGroup> vocGroups = vocGroupApiInterface.list(new VOCGroupSearchRequest());

        if (!vocGroups.isEmpty()) {
            final Map<Integer, String> groups = vocGroups.stream().collect(Collectors.toMap(VocGroup::getSeq, VocGroup::getVocGroupName));

            model.addAttribute("groups", groups);

            if (search.getVocGroupSeq() == null)
                search.setVocGroupSeq(vocGroups.get(0).getSeq());

            final Optional<VocGroup> vocGroupOptional = vocGroups.stream().filter(e -> Objects.equals(e.getSeq(), search.getVocGroupSeq())).findFirst();
            if (!vocGroupOptional.isPresent())
                throw new IllegalArgumentException("존재하지 않는 VOC 그룹입니다: " + search.getVocGroupSeq());

            final VocGroup vocGroup = vocGroupOptional.get();
            model.addAttribute("vocGroup", vocGroup);

            final List<ResearchTree> researchTrees = researchApiInterface.getTrees(vocGroup.getArsResearchId());
            final Integer maxLevel = researchTrees.stream().map(ResearchTree::getLevel).max(Comparator.comparingInt(e -> e)).orElse(0);
            model.addAttribute("maxLevel", maxLevel);

            final Pagination<VocResearchResultEntity> pagination = apiInterface.pagination(search);
            model.addAttribute("pagination", pagination);

            final ResearchItemSearchRequest itemSearch = new ResearchItemSearchRequest();
            itemSearch.setItemIds(researchTrees.stream().mapToInt(ResearchTree::getItemId).boxed().distinct().collect(Collectors.toList()));
            final List<ResearchItem> items = researchItemApiInterface.list(itemSearch);

            final Map<String, Map<String, String>> idToNumberToDescription = new HashMap<>();
            model.addAttribute("idToNumberToDescription", idToNumberToDescription);

            for (ResearchItem e : items)
                idToNumberToDescription.computeIfAbsent(e.getItemId() + "", k -> new HashMap<>()).put(e.getMappingNumber() + "", e.getItemName());
        }
        return "admin/outbound/voc/result/ground";
    }
}
