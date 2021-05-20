package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PDSUploadSummaryResponse {
    private String uploadId;            //업로드아이디
    private Integer pdsGroupId;         //그룹아이디
    private String pdsGroupName;        //PDS 그룹명
    private Timestamp uploadDate;       //업로드일
    private String uploadName;          //업로드 파일명
    private Integer tryCnt;             //업로드 순서
    private Integer uploadCnt;          //업로드 데이터수
    private String uploadStatus;        //업로드상태
    /**
     * @see kr.co.eicn.ippbx.model.enums.UploadStatus
     */
    private String uploadStatusName;
}
