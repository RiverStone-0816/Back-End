package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsCustominfoController;
import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsResearchResultController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.server.model.entity.pds.PdsResearchResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class PdsResearchResultExcel extends AbstractExcel {
    private final List<PdsResearchResultEntity> list;
    private final CommonTypeEntity pdsType;
    private final Integer maxLevel;
    private final List<ResearchItem> researchItems;

    public PdsResearchResultExcel(List<PdsResearchResultEntity> list, CommonTypeEntity pdsType, Integer maxLevel, List<ResearchItem> researchItems) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.list = list;
        this.pdsType = pdsType;
        this.maxLevel = maxLevel;
        this.researchItems = researchItems;
        createBody();
    }

    @SuppressWarnings("unchecked")
    private void createBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<String> firstHeader = new ArrayList<>(Arrays.asList("기본정보", ""));
        firstHeader.add("상담결과필드");
        for (int i = 1; i < pdsType.getFields().size(); i++) firstHeader.add("");
        if (maxLevel > 0) {
            firstHeader.add("설문정보");
            for (int i = 1; i < maxLevel; i++) firstHeader.add("");
        }
        addRow(sheetHeadStyle, firstHeader.toArray());

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 2, 2 + pdsType.getFields().size() - 1));
        if (maxLevel > 0)
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 2 + pdsType.getFields().size(), 2 + pdsType.getFields().size() + maxLevel - 1));

        final List<String> secondHeader = new ArrayList<>(Arrays.asList("발신시간", "전화번호"));
        pdsType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        for (int i = 0; i < maxLevel; i++) secondHeader.add(i+1 + "단계");
        addRow(sheetHeadStyle, secondHeader.toArray());

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = PdsCustominfoController.createCustomIdToFieldNameToValueMap(list.stream().map(PdsResearchResultEntity::getCustomInfo).collect(Collectors.toList()), pdsType);
        final Map<Integer, Map<Integer, String>> seqToPathIndexToValueMap = PdsResearchResultController.createSeqToPathIndexToValueMap(list);

        final Map<String, Map<String, String>> idToNumberToDescription = new HashMap<>();
        for (ResearchItem e : researchItems)
            idToNumberToDescription.computeIfAbsent(e.getItemId() + "", k -> new HashMap<>()).put(e.getMappingNumber() + "", e.getWord());

        for (PdsResearchResultEntity e : list) {
            final List<String> row = new ArrayList<>(Arrays.asList(niceFormat(e.getResultDate()), e.getCustomNumber()));
            pdsType.getFields().forEach(field -> row.add(niceFormat(customIdToFieldNameToValueMap.get(e.getCustomId()).get(field.getFieldId()))));
            for (int i = 1; i <= maxLevel; i++) {
                final String value = seqToPathIndexToValueMap.get(e.getSeq()).get(i);

                if (StringUtils.isNotEmpty(value)) {
                    final String[] split = value.split("_");
                    if (split.length >= 2) {
                        row.add(niceFormat("[" + split[1] + "] "+ idToNumberToDescription.getOrDefault(split[0], new HashMap<>()).getOrDefault(split[1], "")));
                    } else {
                        row.add("");
                    }
                } else {
                    row.add("");
                }
            }
            addRow(defaultStyle, row.toArray());
        }
    }
}
