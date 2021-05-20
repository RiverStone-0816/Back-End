package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class SendFaxEmailHistoryResponse {
    private Long id;            //아이디
    private String receiver;    //수신인
    private List<SendFaxEmailResponse> receiverNumber;      //전화번호, EMAIL주소
    /**
     * @see kr.co.eicn.ippbx.model.enums.SendCategoryType
     */
    private String type;   //발송매체(FAX, EMAIL)
    private String categoryName;   //카테고리명
    private String content;        //유형설명
    /**
     * @see kr.co.eicn.ippbx.model.enums.SendSortType
     */
    private String sendSort;        //발송구분
    private Timestamp sendDate;    //발송일
}
