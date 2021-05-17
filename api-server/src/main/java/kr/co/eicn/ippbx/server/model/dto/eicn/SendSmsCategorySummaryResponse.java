package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SendSmsCategorySummaryResponse {
    private String categoryCode;     //카테고리 코드
    private String categoryName;     //카테고리명
}