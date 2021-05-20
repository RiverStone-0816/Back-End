package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

@Data
public class TalkMsgResponse {
    private Integer seq;
    private String insertTime;      //입력시간
    /**
     * @see kr.co.eicn.ippbx.model.enums.SendReceiveType
     */
    private String sendReceive;
    private String userId;      //상담원id
    private String idName;      //상담원명
    /**
     * @see kr.co.eicn.ippbx.model.enums.MessageType
     */
    private String type;
    private String content;     //내용
}
