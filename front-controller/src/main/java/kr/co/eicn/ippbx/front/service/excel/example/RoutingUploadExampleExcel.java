package kr.co.eicn.ippbx.front.service.excel.example;

import kr.co.eicn.ippbx.front.service.excel.AbstractExcel;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

public class RoutingUploadExampleExcel extends AbstractExcel {
    public RoutingUploadExampleExcel() {
        createBody();
    }

    private void createBody() {
        sheet.createFreezePane(0, 1);
        addRow(sheetHeadStyle, "고객명", "전화번호", "인입방법", "큐번호");

        final Font exampleFont = workbook.createFont();
        exampleFont.setItalic(true);
        exampleFont.setColor(IndexedColors.GREY_25_PERCENT.getIndex());

        final CellStyle exampleStyle = workbook.createCellStyle();
        exampleStyle.cloneStyleFrom(defaultStyle);
        exampleStyle.setFont(exampleFont);

        final List<String> example = new ArrayList<>(Arrays.asList("홍길동1 (선택)", "01012345678 (필수)", "A (필수)", "(공란) (조건부 필수)"));
        addRow(exampleStyle, example.toArray());

        example.clear();
        example.addAll(Arrays.asList("홍길동2 (선택)", "01012345679 (필수)", "B (필수)", "99999999999 (조건부 필수)"));
        addRow(exampleStyle, example.toArray());
    }
}
