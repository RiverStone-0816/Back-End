package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SendSmsCategoryDetailResponse {
    private String categoryCode;     //카테고리 코드
    private String categoryName;     //카테고리명
    private Timestamp createdAt;        //등록일
}