package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
public class NoticeDetailResponse {
    private Long id;
    private String title;    //제목
    private String writer;    //작성자
    private Timestamp createdAt;   //등록일자
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.NoticeType
     */
    private String noticeType;     //공지등록
    private Integer viewCnt;  //조회수
    private String content;   //내용
    private List<FileNameDetailResponse> fileInfo; //첨부파일
}
