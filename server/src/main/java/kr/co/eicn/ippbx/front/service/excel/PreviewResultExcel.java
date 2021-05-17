package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.front.controller.web.admin.outbound.preview.PreviewDataController;
import kr.co.eicn.ippbx.front.util.spring.SpringApplicationContextAware;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.server.model.entity.customdb.PrvResultCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class PreviewResultExcel extends AbstractExcel {
    private final List<PrvResultCustomInfoEntity> list;
    private final CommonTypeEntity previewType;
    private final CommonTypeEntity resultType;

    public PreviewResultExcel(List<PrvResultCustomInfoEntity> rows, CommonTypeEntity previewType, CommonTypeEntity resultType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
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

        final List<String> secondHeader = new ArrayList<>(Arrays.asList("상담등록시간", "상담업데이트시간", "상담자", "통화결과"));
        previewType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        resultType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        addRow(sheetHeadStyle, secondHeader.toArray());

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 4, 4 + previewType.getFields().size() - 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 4 + previewType.getFields().size(), 4 + previewType.getFields().size() + resultType.getFields().size() - 1));

        final RequestGlobal g = SpringApplicationContextAware.requestGlobal();
        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = PreviewDataController.createCustomIdToFieldNameToValueMap(list.stream().map(PrvResultCustomInfoEntity::getCustomInfo).collect(Collectors.toList()), previewType);
        final Map<Integer, Map<String, Object>> seqToFieldNameToValueMap = MaindbResultController.createSeqToFieldNameToValueMap((List<CommonResultCustomInfo>) (List<?>) list, resultType);
        for (PrvResultCustomInfoEntity e : list) {
            final List<String> row = new ArrayList<>(Arrays.asList(niceFormat(e.getResultDate()), niceFormat(e.getUpdateDate()), niceFormat(e.getUserName()), g.messageOf("ResultHangupCause", e.getHangupCause())));

            previewType.getFields().forEach(field -> {
                final Object value = customIdToFieldNameToValueMap.get(e.getCustomInfo().getPrvSysCustomId()).get(field.getFieldId());

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
                final Object value = seqToFieldNameToValueMap.get(e.getSeq()).get(field.getFieldId());

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

            addRow(defaultStyle, row.toArray());
        }
    }
}
