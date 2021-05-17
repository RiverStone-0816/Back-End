package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsResearchResultApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchItemApiInterface;
import kr.co.eicn.ippbx.front.service.excel.PdsResearchResultExcel;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchTree;
import kr.co.eicn.ippbx.server.jooq.pds.tables.pojos.PdsResearchResult;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.server.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.server.model.search.HistoryPdsGroupSearchRequest;
import kr.co.eicn.ippbx.server.model.search.PDSResearchResultSearchRequest;
import kr.co.eicn.ippbx.server.model.search.ResearchItemSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/outbound/pds/research-result")
public class PdsResearchResultController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsResearchResultController.class);

    private final PdsResearchResultApiInterface apiInterface;
    private final CommonTypeApiInterface commonTypeApiInterface;
    private final ResearchApiInterface researchApiInterface;
    private final ResearchItemApiInterface researchItemApiInterface;

    public static Map<Integer, Map<Integer, String>> createSeqToPathIndexToValueMap(List<PdsResearchResultEntity> list) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<Integer, Map<Integer, String>> seqToFieldNameToValueMap = new HashMap<>();

        for (PdsResearchResultEntity row : list) {
            seqToFieldNameToValueMap.put(row.getSeq(), createPathIndexToValueMap(row));
        }
        return seqToFieldNameToValueMap;
    }

    public static Map<Integer, String> createPathIndexToValueMap(PdsResearchResultEntity entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final int MIN_PATH = 1;
        final int MAX_PATH = 20;

        final Map<Integer, String> fieldNameToValueMap = new HashMap<>();

        for (int i = MIN_PATH; i <= MAX_PATH; i++) {
            fieldNameToValueMap.put(i, (String) PdsResearchResult.class.getDeclaredMethod("getMyPath_" + i).invoke(entity));
        }

        return fieldNameToValueMap;
    }

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSResearchResultSearchRequest search) throws IOException, ResultFailException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<HistoryPdsGroup> executingPdsList = apiInterface.addExecuteLists(new HistoryPdsGroupSearchRequest());
        if (executingPdsList.size() > 0) {
            executingPdsList.sort(Comparator.comparing(HistoryPdsGroup::getExecuteName));
            model.addAttribute("executingPdsList", executingPdsList);

            if (search.getExecuteId() == null)
                search.setExecuteId(executingPdsList.get(0).getExecuteId());

            final Optional<HistoryPdsGroup> pdsGroupOptional = executingPdsList.stream().filter(e -> Objects.equals(e.getExecuteId(), search.getExecuteId())).findFirst();
            if (pdsGroupOptional.isPresent()) {

                final HistoryPdsGroup pdsGroup = pdsGroupOptional.get();

                final List<PdsResearchResultEntity> list = apiInterface.getList(search.getExecuteId(), search);
                model.addAttribute("list", list);

                final CommonTypeEntity pdsType = commonTypeApiInterface.get(pdsGroup.getPdsType());
                pdsType.setFields(pdsType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));
                model.addAttribute("pdsType", pdsType);

                model.addAttribute("customIdToFieldNameToValueMap", PdsCustominfoController.createCustomIdToFieldNameToValueMap(list.stream().map(PdsResearchResultEntity::getCustomInfo).collect(Collectors.toList()), pdsType));

                final List<ResearchTree> researchTrees = researchApiInterface.getTrees(Integer.valueOf(pdsGroup.getConnectData()));
                final Integer maxLevel = researchTrees.stream().map(ResearchTree::getLevel).max(Comparator.comparingInt(e -> e)).orElse(0);
                model.addAttribute("maxLevel", maxLevel);

                model.addAttribute("seqToPathIndexToValueMap", createSeqToPathIndexToValueMap(list));

                final ResearchItemSearchRequest itemSearch = new ResearchItemSearchRequest();
                itemSearch.setItemIds(researchTrees.stream().mapToInt(ResearchTree::getItemId).boxed().distinct().collect(Collectors.toList()));
                final List<ResearchItem> items = researchItemApiInterface.list(itemSearch);

                final Map<String, Map<String, String>> idToNumberToDescription = new HashMap<>();
                for (ResearchItem e : items)
                    idToNumberToDescription.computeIfAbsent(e.getItemId() + "", k -> new HashMap<>()).put(e.getMappingNumber() + "", e.getWord());

                model.addAttribute("idToNumberToDescription", idToNumberToDescription);
            }
        }

        return "admin/outbound/pds/research-result/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(PDSResearchResultSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final List<HistoryPdsGroup> executingPdsList = apiInterface.addExecuteLists(new HistoryPdsGroupSearchRequest());
        if (executingPdsList.size() == 0)
            throw new IllegalStateException("PDS 실행이력이 존재하지 않습니다.");

        if (search.getExecuteId() == null)
            search.setExecuteId(executingPdsList.get(0).getExecuteId());

        final Optional<HistoryPdsGroup> pdsGroupOptional = executingPdsList.stream().filter(e -> Objects.equals(e.getExecuteId(), search.getExecuteId())).findFirst();
        if (!pdsGroupOptional.isPresent())
            throw new IllegalArgumentException("존재하지 않는 PDS 실행이력입니다.");

        final HistoryPdsGroup pdsGroup = pdsGroupOptional.get();

        final List<PdsResearchResultEntity> list = apiInterface.getList(search.getExecuteId(), search);
        final CommonTypeEntity pdsType = commonTypeApiInterface.get(pdsGroup.getPdsType());
        pdsType.setFields(pdsType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));

        final List<ResearchTree> researchTrees = researchApiInterface.getTrees(Integer.valueOf(pdsGroup.getConnectData()));
        final Integer maxLevel = researchTrees.stream().map(ResearchTree::getLevel).max(Comparator.comparingInt(e -> e)).orElse(0);

        final ResearchItemSearchRequest itemSearch = new ResearchItemSearchRequest();
        itemSearch.setItemIds(researchTrees.stream().mapToInt(ResearchTree::getItemId).boxed().distinct().collect(Collectors.toList()));
        final List<ResearchItem> researchItems = researchItemApiInterface.list(itemSearch);

        new PdsResearchResultExcel(list, pdsType, maxLevel, researchItems).generator(response, "결과이력");
    }
}
