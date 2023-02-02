package kr.co.eicn.ippbx.front.service.excel;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.model.form.CommonCodeFormRequest;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class MultiCodeExcelUploadService implements ExcelUploadServiceInterface {
    public List<CommonCodeFormRequest> convertExcelToList(FileForm file) throws IOException, InvalidFormatException {
        final File excel = new File(file.getFilePath());
        DataFormatter dataFormatter = new DataFormatter();

        return convertExcelToResponse(excel, row -> {
            CommonCodeFormRequest request = new CommonCodeFormRequest();

            if (row.getCell(0) != null)
                request.setCodeId(dataFormatter.formatCellValue(row.getCell(0)));

            if (row.getCell(1) != null)
                request.setCodeName(dataFormatter.formatCellValue(row.getCell(1)));

            if (row.getCell(2) != null)
                request.setSequence(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(2))));

            if (row.getCell(3) != null)
                request.setScript(dataFormatter.formatCellValue(row.getCell(3)));

            if (row.getCell(4) != null)
                request.setHide(dataFormatter.formatCellValue(row.getCell(4)));

            return request;
        });
    }
}
