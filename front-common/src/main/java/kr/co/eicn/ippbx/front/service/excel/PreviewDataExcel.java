package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.controller.web.admin.outbound.preview.PreviewDataController;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonPrvCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.model.entity.customdb.PrvCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class PreviewDataExcel extends AbstractExcel {
    private final List<PrvCustomInfoEntity> list;
    private final CommonTypeEntity previewType;
    private final CommonTypeEntity resultType;

    public PreviewDataExcel(List<PrvCustomInfoEntity> rows, CommonTypeEntity previewType, CommonTypeEntity resultType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.list = rows;
        this.previewType = previewType;
        this.resultType = resultType;
        createBody();
    }

    @SuppressWarnings("unchecked")
    private void createBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<String> firstHeader = new ArrayList<>(Arrays.asList("기본정보", "", ""));
        firstHeader.add("고객정보필드");
        for (int i = 1; i < previewType.getFields().size(); i++) firstHeader.add("");
        firstHeader.add("상담결과필드");
        for (int i = 1; i < resultType.getFields().size(); i++) firstHeader.add("");
        addRow(sheetHeadStyle, firstHeader.toArray());

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3, 3 + previewType.getFields().size() - 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 3 + previewType.getFields().size(), 3 + previewType.getFields().size() + resultType.getFields().size() - 1));

        final List<String> secondHeader = new ArrayList<>(Arrays.asList("데이터생성일", "담당자", "마지막상담일"));
        previewType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        resultType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        addRow(sheetHeadStyle, secondHeader.toArray());

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = PreviewDataController.createCustomIdToFieldNameToValueMap((List<CommonPrvCustomInfo>) (List<?>) list, previewType);
        final Map<Integer, Map<String, Object>> seqToFieldNameToValueMap = MaindbResultController.createSeqToFieldNameToValueMap(list.stream().map(PrvCustomInfoEntity::getResult).filter(Objects::nonNull).collect(Collectors.toList()), resultType);
        for (PrvCustomInfoEntity e : list) {
            final List<String> row = new ArrayList<>(Arrays.asList(niceFormat(e.getPrvSysUploadDate()), niceFormat(e.getPrvSysDamdangName()), niceFormat(e.getPrvSysLastResultDate())));

            previewType.getFields().forEach(field -> {
                final Object value = customIdToFieldNameToValueMap.get(e.getPrvSysCustomId()).get(field.getFieldId());

                if (Objects.equals(field.getFieldType(), "CODE")) {
                    row.add(field.getCodes().stream().filter(e2 -> Objects.equals(e2.getCodeId(), value)).map(CommonCode::getCodeName).findFirst().orElse(""));
                } else if (Objects.equals(field.getFieldType(), "MULTICODE")) {
                    if (value == null) {
                        row.add("");
                    } else {
                        final StringBuilder builder = new StringBuilder();
                        for (String v : value.toString().split(","))
                            builder.append(field.getCodes().stream().filter(e2 -> Objects.equals(e2.getCodeId(), v)).map(CommonCode::getCodeName).findFirst().orElse("")).append(" ");
                        row.add(builder.toString());
                    }
                } else {
                    row.add(niceFormat(value));
                }
            });

            resultType.getFields().forEach(field -> {
                if (e.getResult() != null && e.getResult().getSeq() != null) {
                    final Object value = seqToFieldNameToValueMap.get(e.getResult().getSeq()).get(field.getFieldId());

                    if (Objects.equals(field.getFieldType(), "CODE")) {
                        row.add(field.getCodes().stream().filter(e2 -> Objects.equals(e2.getCodeId(), value)).map(CommonCode::getCodeName).findFirst().orElse(""));
                    } else if (Objects.equals(field.getFieldType(), "MULTICODE")) {
                        if (value == null) {
                            row.add("");
                        } else {
                            final StringBuilder builder = new StringBuilder();
                            for (String v : value.toString().split(","))
                                builder.append(field.getCodes().stream().filter(e2 -> Objects.equals(e2.getCodeId(), v)).map(CommonCode::getCodeName).findFirst().orElse("")).append(" ");
                            row.add(builder.toString());
                        }
                    } else {
                        row.add(niceFormat(value));
                    }
                } else {
                    row.add("");
                }
            });

            addRow(defaultStyle, row.toArray());
        }
    }
}
