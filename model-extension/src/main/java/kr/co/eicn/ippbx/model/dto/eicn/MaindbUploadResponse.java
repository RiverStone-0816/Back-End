package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MaindbUploadResponse {
    private String uploadId;            //아이디
    private String maindbGroupName;     //고객DB 그룹명
    private Timestamp uploadDate;       //업로드일
    private String uploadName;          //업로드파일명
    private Integer tryCnt;             //업로드순서
    private Integer uploadCnt;          //업로드 데이터수
    /**
     * @see kr.co.eicn.ippbx.model.enums.UploadStatus
     */
    private String uploadStatus;        //업로드상태
}
