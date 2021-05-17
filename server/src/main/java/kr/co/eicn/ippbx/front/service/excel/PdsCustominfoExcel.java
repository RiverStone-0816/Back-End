package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.controller.web.admin.outbound.pds.PdsCustominfoController;
import kr.co.eicn.ippbx.server.jooq.pds.tables.pojos.PdsCustomInfo;
import kr.co.eicn.ippbx.server.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.server.model.entity.pds.PDSCustomInfoEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PdsCustominfoExcel extends AbstractExcel {
    private final List<PDSCustomInfoEntity> list;
    private final CommonTypeEntity customDbType;

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

        final Map<String, Map<String, Object>> customIdToFieldNameToValueMap = PdsCustominfoController.createCustomIdToFieldNameToValueMap((List<PdsCustomInfo>) (List<?>) list, customDbType);
        for (PDSCustomInfoEntity e : list) {
            final List<String> row = new ArrayList<>(Collections.singletonList(niceFormat(e.getPdsSysUploadDate())));
            customDbType.getFields().forEach(field -> row.add(niceFormat(customIdToFieldNameToValueMap.get(e.getPdsSysCustomId()).get(field.getFieldId()))));
            addRow(defaultStyle, row.toArray());
        }
    }
}
