package kr.co.eicn.ippbx.model;

import kr.co.eicn.ippbx.model.enums.RecordFileKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordFile {
    private String         filePath; // 파일명을 포함한 파일경로
    /**
     * @see RecordFileKind
     */
    private RecordFileKind kind; // 녹취구분 전수녹취, 부분녹취
    private Integer        index; // 파일 index
    private Integer        cdr;
    private String         uniqueid;
    private String         dstUniqueid;
}
