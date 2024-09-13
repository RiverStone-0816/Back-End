package kr.co.eicn.ippbx.front.service.excel.example;

import kr.co.eicn.ippbx.front.service.excel.AbstractExcel;
import kr.co.eicn.ippbx.model.entity.eicn.CommonFieldEntity;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MaindbCustomUploadExampleExcel extends AbstractExcel {
    private final CommonTypeEntity customDbType;

    public MaindbCustomUploadExampleExcel(CommonTypeEntity customDbType) {
        this.customDbType = customDbType;
        createBody();
    }

    private void createBody() {
        sheet.createFreezePane(0, 1);

        final List<String> headers = new ArrayList<>();

        int index = 0;
        final Set<Integer> requiredFieldsIndexes = new HashSet<>();
        for (CommonFieldEntity field : customDbType.getFields()) {
            if (field.getIsneed().equals("Y"))
                requiredFieldsIndexes.add(index);

            headers.add(field.getFieldInfo());
            index++;
        }

        headers.add("전화번호");
        requiredFieldsIndexes.add(index);

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
