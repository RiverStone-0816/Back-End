package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbDataController;
import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbResultController;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbMultichannelInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class MaindbResultExcel extends AbstractExcel {
    private final List<ResultCustomInfoEntity> list;
    private final CommonTypeEntity customDbType;
    private final CommonTypeEntity resultType;

    public MaindbResultExcel(List<ResultCustomInfoEntity> rows, CommonTypeEntity customDbType, CommonTypeEntity resultType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.list = rows;
        this.customDbType = customDbType;
        this.resultType = resultType;
        createBody();
    }

    @SuppressWarnings("unchecked")
    private void createBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<String> firstHeader = new ArrayList<>(Arrays.asList("상담기본정보", "", "", "", ""));
        firstHeader.add("상담결과필드");
        for (int i = 1; i < resultType.getFields().size(); i++) firstHeader.add("");
        firstHeader.add("고객정보필드");
        for (int i = 1; i < customDbType.getFields().size(); i++) firstHeader.add("");
        firstHeader.addAll(Arrays.asList("채널정보", "", ""));
        addRow(sheetHeadStyle, firstHeader.toArray());

        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        if (resultType.getFields().size() > 1)
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 5, 5 + resultType.getFields().size() - 1));
        if (customDbType.getFields().size() > 1)
            getSheet().addMergedRegion(new CellRangeAddress(0, 0, 5 + resultType.getFields().size(), 5 + resultType.getFields().size() + customDbType.getFields().size() - 1));
        getSheet().addMergedRegion(new CellRangeAddress(0, 0, 5 + resultType.getFields().size() + customDbType.getFields().size(), 5 + resultType.getFields().size() + customDbType.getFields().size() + 2));

        final List<String> secondHeader = new ArrayList<>(Arrays.asList("채널", "수/발신", "상담등록시간", "상담원", "고객채널정보"));
        resultType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        customDbType.getFields().forEach(field -> secondHeader.add(field.getFieldInfo()));
        secondHeader.addAll(Arrays.asList("전화번호", "이메일", "상담톡아이디"));
        addRow(sheetHeadStyle, secondHeader.toArray());

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = MaindbDataController.createCustomIdToFieldNameToValueMap(list.stream().map(ResultCustomInfoEntity::getCustomInfo).collect(Collectors.toList()), customDbType);
        final Map<Integer, Map<String, Object>> seqToFieldNameToValueMap = MaindbResultController.createSeqToFieldNameToValueMap((List<CommonResultCustomInfo>) (List<?>) list, resultType);
        for (ResultCustomInfoEntity e : list) {
            final List<String> row = new ArrayList<>(Arrays.asList(e.getGroupKind(), StringUtils.isNotEmpty(e.getCallType()) ? (e.getCallType().equals("I") ? "수신" : "발신") : "",
                    niceFormat(e.getResultDate()), e.getUserName(), e.getCustomNumber()));
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
            customDbType.getFields().forEach(field -> {
                final Object value = customIdToFieldNameToValueMap.get(e.getCustomId()).get(field.getFieldId());

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

            for (String channelType : Arrays.asList("PHONE", "EMAIL", "TALK")) {
                if (e.getMultichannelList() != null) {
                    row.add(niceFormat(e.getMultichannelList().stream()
                            .filter(e2 -> Objects.equals(e2.getChannelType(), channelType))
                            .map(CommonMaindbMultichannelInfo::getChannelData)
                            .reduce((v, a) -> v + " " + a).orElse("")));
                } else {
                    row.add("");
                }
            }

            CellStyle rowStyle = defaultStyle;
            rowStyle.setWrapText(true);
            addRow(rowStyle, row.toArray());
        }
    }
}
