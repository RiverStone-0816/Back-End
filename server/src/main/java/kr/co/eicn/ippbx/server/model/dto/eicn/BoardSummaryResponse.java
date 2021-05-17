package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Date;

@Data
public class BoardSummaryResponse {
    private Long id;       //글번호
    private String noticeType; //공지등록
    private String title;    //제목
    private Date createdAt;   //등록일
    private String writer;    //작성자
    private Integer viewCnt;     //조회수
}
