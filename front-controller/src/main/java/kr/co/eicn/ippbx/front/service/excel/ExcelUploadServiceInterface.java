package kr.co.eicn.ippbx.front.service.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface ExcelUploadServiceInterface {
    default <RESPONSE> List<RESPONSE> convertExcelToResponse(File excel, Function<XSSFRow, RESPONSE> function) throws IOException, InvalidFormatException {
        OPCPackage opcPackage = OPCPackage.open(excel);
        XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        List<RESPONSE> result = new ArrayList<>();

        //첫줄은 필드 설명란이기 때문에 i = 0이 아닌 1
        for (int i = 1; i <= sheetAt.getLastRowNum(); i++)
            result.add(function.apply(sheetAt.getRow(i)));

        return result;
    }
}
