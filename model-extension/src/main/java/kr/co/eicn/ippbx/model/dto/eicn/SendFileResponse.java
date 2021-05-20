package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.SendCategoryType;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SendFileResponse {
    private Long   id;
    private String sendName;    //발송물명
    private SendCategoryType sendMedia;   //발송매체(FAX, EMAIL)
    private String categoryCode;    //카테고리코드
    private String categoryName;    //카테고리명
    private String desc;        //유형설명
    private Timestamp createdAt;   //등록일
    private String filePath;
    private String fileName;    //등록 파일명
}
