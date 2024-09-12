package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsCustominfoController;
import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsResultController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.ResultCustomInfo;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;
import java.util.stream.Collectors;

public class PdsResultExcel extends AbstractExcel {
    private final List<PDSResultCustomInfoEntity> list;
    private final CommonTypeEntity                pdsType;
    private final CommonTypeEntity                resultType;

    @SneakyThrows
    public PdsResultExcel(List<PDSResultCustomInfoEntity> list, CommonTypeEntity pdsType, CommonTypeEntity resultType) {
        this.list = list;
        this.pdsType = pdsType;
        this.resultType = resultType;
        createBody();
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void createBody() {
        final List<String> firstHeader = new ArrayList<>(Arrays.asList("번호", "상담기본정보", ""));
        firstHeader.add("상담결과필드");
        for (int i = 1; i < resultType.getFields().size(); i++) firstHeader.add("");
        firstHeader.add("고객정보필드");
        for (int i = 1; i < pdsType.getFields().size(); i++) firstHeader.add("");
        addRow(sheetHeadStyle, firstHeader.toArray());

        getSheet().addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
        if (resultType.getFields().size() > 1)
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 3 + resultType.getFields().size() - 1));
        if (pdsType.getFields().size() > 1)
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3 + resultType.getFields().size(), 3 + resultType.getFields().size() + pdsType.getFields().size() - 1));

        final List<String> secondHeader = new ArrayList<>(Arrays.asList("", "상담등록시간", "상담원"));
        resultType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        pdsType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        addRow(sheetHeadStyle, secondHeader.toArray());

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = PdsCustominfoController.createCustomIdToFieldNameToValueMap(list.stream().map(PDSResultCustomInfoEntity::getCustomInfo).collect(Collectors.toList()), pdsType);
        final Map<Integer, Map<String, Object>> seqToFieldNameToValueMap = PdsResultController.createSeqToFieldNameToValueMap(list, resultType);
        int i = 0;
        for (PDSResultCustomInfoEntity e : list) {
            i++;
            final List<String> row = new ArrayList<>(Arrays.asList(String.valueOf(i), niceFormat(e.getResultDate()), e.getUserName()));
            resultType.getFields().forEach(field -> {
                final Object value = seqToFieldNameToValueMap.get(e.getSeq()).get(field.getFieldId());

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
