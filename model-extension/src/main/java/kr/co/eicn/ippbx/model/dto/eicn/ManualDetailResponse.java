package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ManualDetailResponse {
    private Long id;
    private String title;     //제목
    private String writer;    //작성자
    private Timestamp createdAt;   //등록일자
    private Integer viewCnt;  //조회수
    private List<FileNameDetailResponse> fileInfo; //첨부파일
}
