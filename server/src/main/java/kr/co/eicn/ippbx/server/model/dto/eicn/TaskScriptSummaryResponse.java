package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.Date;

@Data
public class TaskScriptSummaryResponse {
    private Long id;
    private Long categoryId;   //카테고리
    private String title;   //제목
    private String tag;     //태그
    private Date createdAt; //등록일
}
