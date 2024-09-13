package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsCustominfoController;
import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsResearchResultController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.pds.PdsResearchResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class PdsResearchResultExcel extends AbstractExcel {
    private final List<PdsResearchResultEntity> list;
    private final CommonTypeEntity              pdsType;
    private final Integer                       maxLevel;
    private final List<ResearchItem>            researchItems;

    public PdsResearchResultExcel(List<PdsResearchResultEntity> list, CommonTypeEntity pdsType, Integer maxLevel, List<ResearchItem> researchItems) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.list = list;
        this.pdsType = pdsType;
        this.maxLevel = maxLevel;
        this.researchItems = researchItems;
        createBody();
    }

    @SuppressWarnings("unchecked")
    private void createBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<String> firstHeader = new ArrayList<>(Arrays.asList("번호", "기본정보", ""));
        if (maxLevel > 0) {
            firstHeader.add("설문정보");
            for (int i = 1; i < maxLevel; i++) firstHeader.add("");
        }
        firstHeader.add("고객정보필드");
        for (int i = 1; i < pdsType.getFields().size(); i++) firstHeader.add("");

        addRow(sheetHeadStyle, firstHeader.toArray());

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        if (maxLevel > 1)
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 3 + maxLevel - 1));
        if (pdsType.getFields().size() > 1)
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3 + maxLevel, 3 + maxLevel + pdsType.getFields().size() - 1));

        final List<String> secondHeader = new ArrayList<>(Arrays.asList("", "설문등록시간", "수신번호"));
        for (int i = 0; i < maxLevel; i++) secondHeader.add(i + 1 + "단계");
        pdsType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        addRow(sheetHeadStyle, secondHeader.toArray());

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = PdsCustominfoController.createCustomIdToFieldNameToValueMap(list.stream().map(PdsResearchResultEntity::getCustomInfo).collect(Collectors.toList()), pdsType);
        final Map<Integer, Map<Integer, String>> seqToPathIndexToValueMap = PdsResearchResultController.createSeqToPathIndexToValueMap(list);

        final Map<String, Map<String, String>> idToNumberToDescription = new HashMap<>();
        for (ResearchItem e : researchItems)
            idToNumberToDescription.computeIfAbsent(e.getItemId() + "", k -> new HashMap<>()).put(e.getMappingNumber() + "", e.getWord());

        int i = 0;
        for (PdsResearchResultEntity e : list) {
            i++;
            final List<String> row = new ArrayList<>(Arrays.asList(String.valueOf(i), niceFormat(e.getResultDate()), e.getCustomNumber()));

            for (int j = 1; j <= maxLevel; j++) {
                final String value = seqToPathIndexToValueMap.get(e.getSeq()).get(j);

                if (StringUtils.isNotEmpty(value)) {
                    final String[] split = value.split("_");
                    if (split.length >= 2)
                        row.add(niceFormat("[" + split[1] + "] " + idToNumberToDescription.getOrDefault(split[0], new HashMap<>()).getOrDefault(split[1], "")));
                    else
                        row.add("");
                } else
                    row.add("");
            }

            pdsType.getFields().forEach(field -> {
                final Object value = customIdToFieldNameToValueMap.get(e.getCustomId()).get(field.getFieldId());

                if (Objects.equals(field.getFieldType(), "CODE"))
                    row.add(field.getCodes().stream().filter(e2 -> Objects.equals(e2.getCodeId(), value)).map(CommonCode::getCodeName).findFirst().orElse(""));
                else if (Objects.equals(field.getFieldType(), "MULTICODE")) {
                    if (value == null) {
                        row.add("");
                    } else {
                        final StringBuilder builder = new StringBuilder();
                        for (String v : value.toString().split(","))
                            builder.append(field.getCodes().stream().filter(e2 -> Objects.equals(e2.getCodeId(), v)).map(CommonCode::getCodeName).findFirst().orElse("")).append(" ");
                        row.add(builder.toString());
                    }
                } else
                    row.add(niceFormat(value));
            });

            addRow(defaultStyle, row.toArray());
        }
    }
}
