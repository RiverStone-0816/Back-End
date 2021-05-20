package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TaskScriptDetailResponse {
    private Long id;
    private Long categoryId;   //카테고리
    private String title;   //제목
    private Timestamp createdAt; //등록일
    private String tag;     //태그
    private String content;      //내용
    private List<FileNameDetailResponse> fileInfo;     //첨부파일
}
