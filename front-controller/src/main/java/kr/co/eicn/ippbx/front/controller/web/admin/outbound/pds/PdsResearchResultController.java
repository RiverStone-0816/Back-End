package kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.PDSResearchResultSearch;
import kr.co.eicn.ippbx.model.dto.eicn.HistoryPdsResearchGroupResponse;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsResearchResultApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.outbound.research.ResearchItemApiInterface;
import kr.co.eicn.ippbx.front.service.excel.PdsResearchResultExcel;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchTree;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsResearchResult;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.model.search.ResearchItemSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
    private final CommonTypeApiInterface        commonTypeApiInterface;
    private final ResearchApiInterface          researchApiInterface;
    private final ResearchItemApiInterface      researchItemApiInterface;

    public static <T extends PdsResearchResult> Map<Integer, Map<Integer, String>> createSeqToPathIndexToValueMap(List<T> list) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Map<Integer, Map<Integer, String>> seqToFieldNameToValueMap = new HashMap<>();

        for (T row : list) {
            seqToFieldNameToValueMap.put(row.getSeq(), createPathIndexToValueMap(row));
        }
        return seqToFieldNameToValueMap;
    }

    public static Map<Integer, String> createPathIndexToValueMap(PdsResearchResult entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final int MIN_PATH = 1;
        final int MAX_PATH = 20;

        final Map<Integer, String> fieldNameToValueMap = new HashMap<>();

        for (int i = MIN_PATH; i <= MAX_PATH; i++) {
            fieldNameToValueMap.put(i, (String) PdsResearchResult.class.getDeclaredMethod("getMyPath_" + i).invoke(entity));
        }

        return fieldNameToValueMap;
    }

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") PDSResearchResultSearch search) throws IOException, ResultFailException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<HistoryPdsResearchGroupResponse> executingPdsList = apiInterface.getExecutingPdsList();

        if (CollectionUtils.isEmpty(executingPdsList))
            return "admin/outbound/pds/research-result/ground";

        model.addAttribute("executingPdsList", new MapToLinkedHashMap().toLinkedHashMapByValue(executingPdsList.stream().collect(Collectors.toMap(HistoryPdsResearchGroupResponse::getExecuteId, HistoryPdsResearchGroupResponse::getExecuteName))));

        if (search.getExecuteId() == null)
            search.setExecuteId(executingPdsList.get(0).getExecuteId());

        final Optional<HistoryPdsResearchGroupResponse> executePDSGroupEntityOptional = executingPdsList.stream().filter(e -> Objects.equals(e.getExecuteId(), search.getExecuteId())).findFirst();
        if (!executePDSGroupEntityOptional.isPresent())
            throw new IllegalArgumentException("존재하지 않는 PDS실행정보입니다: " + search.getExecuteId());

        final HistoryPdsResearchGroupResponse executingPds = executePDSGroupEntityOptional.get();

        final CommonTypeEntity pdsType = commonTypeApiInterface.get(executingPds.getPdsType());
        pdsType.setFields(pdsType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));
        model.addAttribute("pdsType", pdsType);

        final List<ResearchTree> researchTrees = researchApiInterface.getTrees(Integer.valueOf(executingPds.getConnectData()));
        final Integer maxLevel = researchTrees.stream().map(ResearchTree::getLevel).max(Comparator.comparingInt(e -> e)).orElse(0);
        model.addAttribute("maxLevel", maxLevel);

        final ResearchItemSearchRequest itemSearch = new ResearchItemSearchRequest();
        itemSearch.setItemIds(researchTrees.stream().mapToInt(ResearchTree::getItemId).boxed().distinct().collect(Collectors.toList()));
        final List<ResearchItem> items = researchItemApiInterface.list(itemSearch);

        final Map<String, Map<String, String>> idToNumberToDescription = new HashMap<>();
        items.forEach(e -> idToNumberToDescription.computeIfAbsent(e.getItemId() + "", k -> new HashMap<>()).put(e.getMappingNumber() + "", e.getWord()));
        model.addAttribute("idToNumberToDescription", idToNumberToDescription);

        final Pagination<PdsResearchResultEntity> pagination = apiInterface.pagination(search.getExecuteId(), search.convertToRequest(""));
        model.addAttribute("pagination", pagination);

        model.addAttribute("customIdToFieldNameToValueMap", PdsCustominfoController.createCustomIdToFieldNameToValueMap(pagination.getRows().stream().map(PdsResearchResultEntity::getCustomInfo).collect(Collectors.toList()), pdsType));
        model.addAttribute("seqToPathIndexToValueMap", createSeqToPathIndexToValueMap(pagination.getRows()));

        return "admin/outbound/pds/research-result/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(PDSResearchResultSearch search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final List<HistoryPdsResearchGroupResponse> executingPdsList = apiInterface.getExecutingPdsList();

        if (CollectionUtils.isEmpty(executingPdsList))
            throw new IllegalStateException("실행한 PDS가 존재하지 않습니다.");

        if (search.getExecuteId() == null)
            search.setExecuteId(executingPdsList.get(0).getExecuteId());

        final Optional<HistoryPdsResearchGroupResponse> executePDSGroupEntityOptional = executingPdsList.stream().filter(e -> Objects.equals(e.getExecuteId(), search.getExecuteId())).findFirst();
        if (!executePDSGroupEntityOptional.isPresent())
            throw new IllegalArgumentException("존재하지 않는 PDS실행정보입니다: " + search.getExecuteId());

        final HistoryPdsResearchGroupResponse executingPds = executePDSGroupEntityOptional.get();

        final CommonTypeEntity pdsType = commonTypeApiInterface.get(executingPds.getPdsType());
        pdsType.setFields(pdsType.getFields().stream().filter(e -> "Y".equals(e.getIsdisplayList())).collect(Collectors.toList()));

        final List<ResearchTree> researchTrees = researchApiInterface.getTrees(Integer.valueOf(executingPds.getConnectData()));
        final Integer maxLevel = researchTrees.stream().map(ResearchTree::getLevel).max(Comparator.comparingInt(e -> e)).orElse(0);

        final ResearchItemSearchRequest itemSearch = new ResearchItemSearchRequest();
        itemSearch.setItemIds(researchTrees.stream().mapToInt(ResearchTree::getItemId).boxed().distinct().collect(Collectors.toList()));
        final List<ResearchItem> items = researchItemApiInterface.list(itemSearch);

        final Map<String, Map<String, String>> idToNumberToDescription = new HashMap<>();
        items.forEach(e -> idToNumberToDescription.computeIfAbsent(e.getItemId() + "", k -> new HashMap<>()).put(e.getMappingNumber() + "", e.getWord()));

        final Pagination<PdsResearchResultEntity> pagination = apiInterface.pagination(search.getExecuteId(), search.convertToRequest(""));

        new PdsResearchResultExcel(pagination.getRows(), pdsType, maxLevel, items).generator(response, "설문결과이력");
    }
}
