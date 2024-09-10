package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsCustominfoController;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbMultichannelInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonCode;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSCustomInfoEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PdsCustominfoExcel extends AbstractExcel {
    private final List<PDSCustomInfoEntity> list;
    private final CommonTypeEntity          customDbType;

    public PdsCustominfoExcel(List<PDSCustomInfoEntity> list, CommonTypeEntity customDbType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this.list = list;
        this.customDbType = customDbType;
        createBody();
    }

    @SuppressWarnings("unchecked")
    private void createBody() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final List<String> headers = new ArrayList<>(Collections.singletonList("데이터생성일"));
        customDbType.getFields().forEach(field -> headers.add(field.getFieldInfo()));
        addRow(sheetHeadStyle, headers.toArray());

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = PdsCustominfoController.createCustomIdToFieldNameToValueMap(list, customDbType);

        for (PDSCustomInfoEntity e : list) {
            final List<String> row = new ArrayList<>(Collections.singletonList(niceFormat(e.getPdsSysUploadDate())));
            customDbType.getFields().forEach(field -> {
                if (field.getFieldType().equals("CODE")) {
                    row.add(field.getCodes().stream().filter(code -> code.getCodeId().equals(niceFormat(customIdToFieldNameToValueMap.get(e.getPdsSysCustomId()).get(field.getFieldId())))).map(CommonCode::getCodeName).findFirst().orElse(""));
                } else if (field.getFieldType().equals("MULTICODE")) {
                    String[] temparray = niceFormat(customIdToFieldNameToValueMap.get(e.getPdsSysCustomId()).get(field.getFieldId())).split(",");
                    String codeName = ",";
                    for (int i = 0; i < temparray.length; i++) {
                        int finalI = i;
                        codeName = codeName + "," + field.getCodes().stream().filter(code -> code.getCodeId().equals(temparray[finalI])).map(CommonCode::getCodeName).findFirst().orElse("");
                    }
                    row.add(codeName.substring(2));
                } else {
                    row.add(niceFormat(customIdToFieldNameToValueMap.get(e.getPdsSysCustomId()).get(field.getFieldId())));
                }
            });
            addRow(defaultStyle, row.toArray());
        }
    }
}
