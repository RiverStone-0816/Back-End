package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.application.maindb.MaindbDataController;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonMaindbCustomInfo;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonMaindbMultichannelInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.server.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MaindbDataExcel extends AbstractExcel {
    private final List<MaindbCustomInfoEntity> list;
    private final CommonTypeEntity customDbType;

    public MaindbDataExcel(List<MaindbCustomInfoEntity> list, CommonTypeEntity customDbType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.list = list;
        this.customDbType = customDbType;
        createBody();
    }

    @SuppressWarnings("unchecked")
    private void createBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<String> headers = new ArrayList<>(Collections.singletonList("데이터생성일"));
        customDbType.getFields().forEach(field -> headers.add(field.getFieldInfo()));
        headers.addAll(Arrays.asList("전화번호", "이메일", "상담톡아이디"));
        addRow(sheetHeadStyle, headers.toArray());

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = MaindbDataController.createCustomIdToFieldNameToValueMap((List<CommonMaindbCustomInfo>) (List<?>) list, customDbType);
        for (MaindbCustomInfoEntity e : list) {
            final List<String> row = new ArrayList<>(Collections.singletonList(niceFormat(e.getMaindbSysUploadDate())));
            customDbType.getFields().forEach(field -> {
                    if (field.getFieldType().equals("CODE")) {
                        row.add(field.getCodes().stream().filter(code -> code.getCodeId().equals(niceFormat(customIdToFieldNameToValueMap.get(e.getMaindbSysCustomId()).get(field.getFieldId())))).map(CommonCode::getCodeName).findFirst().orElse(""));
                    } else if (field.getFieldType().equals("MULTICODE")) {
                        String[] temparray = niceFormat(customIdToFieldNameToValueMap.get(e.getMaindbSysCustomId()).get(field.getFieldId())).split(",");
                        String codeName = ",";
                        for(int i=0;i<temparray.length;i++){
                            int finalI = i;
                            codeName = codeName +","+ field.getCodes().stream().filter(code -> code.getCodeId().equals(temparray[finalI])).map(CommonCode::getCodeName).findFirst().orElse("");
                        }
                        row.add(codeName.substring(2));
                    } else {
                        row.add(niceFormat(customIdToFieldNameToValueMap.get(e.getMaindbSysCustomId()).get(field.getFieldId())));
                    }
                }
            );

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

            addRow(defaultStyle, row.toArray());
        }
    }
}
