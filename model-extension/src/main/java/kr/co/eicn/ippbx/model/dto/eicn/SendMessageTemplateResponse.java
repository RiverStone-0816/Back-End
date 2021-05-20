package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SendMessageTemplateResponse {
    private Integer id;             //아이디
    private String categoryCode;    //카테고리코드
    private String categoryName;    //카테고리명
    private String content;         //메시지
    private Timestamp createdAt;       //등록일
}
