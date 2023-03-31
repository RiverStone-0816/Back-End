package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SendMessageHistoryResponse {
    private Integer id;         //아이디
    private String receiver;    //수신인
    private String target;      //수신번호
    private String content;     //문구내용
    /**
     * @see kr.co.eicn.ippbx.model.enums.SendSortType
     */
    private String sendSort;     //발송구분(D:직접입력, C:카테고리입력)
    private String sendType;
    private String resMessage;
    private Timestamp sendDate;        //발송일
}
