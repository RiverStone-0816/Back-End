package kr.co.eicn.ippbx.front.service.excel.example;

import kr.co.eicn.ippbx.front.service.excel.AbstractExcel;
import kr.co.eicn.ippbx.model.dto.eicn.PDSGroupDetailResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.enums.PDSGroupRidKind;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PdsCustomUploadExampleExcel extends AbstractExcel {
    private final PDSGroupDetailResponse group;
    private final CommonTypeEntity       customDbType;

    public PdsCustomUploadExampleExcel(PDSGroupDetailResponse group, CommonTypeEntity customDbType) {
        this.group = group;
        this.customDbType = customDbType;
        createBody();
    }

    private void createBody() {
        sheet.createFreezePane(0, 1);

        final List<String> headers = new ArrayList<>();

        int index = 0;
        final Set<Integer> requiredFieldsIndexes = new HashSet<>();
        if (group.getRidKind().equals(PDSGroupRidKind.FIELD.getCode())) {
            headers.add("RID");
            requiredFieldsIndexes.add(0);
            index++;
        }

        for (CommonFieldEntity field : customDbType.getFields()) {
            if (field.getIsneed().equals("Y"))
                requiredFieldsIndexes.add(index);

            headers.add(field.getFieldInfo());
            index++;
        }
        addRow(sheetHeadStyle, headers.toArray());

        final CellStyle emphasisStyle = workbook.createCellStyle();
        emphasisStyle.cloneStyleFrom(sheetHeadStyle);
        emphasisStyle.setFillForegroundColor(IndexedColors.CORAL.getIndex());

        final Row targetRow = sheet.getRow(0);
        requiredFieldsIndexes.forEach(e -> {
            final Cell targetCell = targetRow.getCell(e);
            targetCell.setCellStyle(emphasisStyle);
        });
    }
}