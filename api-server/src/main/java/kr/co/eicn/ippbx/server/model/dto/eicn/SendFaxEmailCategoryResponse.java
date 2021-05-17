package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.enums.SendCategoryType;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SendFaxEmailCategoryResponse {
    private SendCategoryType sendMedia;        //발송매체
    private String categoryName;     //카테고리명
    private String categoryCode;     //카테고리 코드
    private Timestamp createdAt;        //등록일
}
