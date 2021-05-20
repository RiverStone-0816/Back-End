package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.CallbackStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CallbackHistoryResponse {
    private Integer seq;            //seq값
    private String callerNumber;    //수신번호
    private String callbackNumber;  //콜백번호
    private String queueName;        //인입큐명
    private String svcName;         //인입서비스명
    private String idName;          //상담원명
    private CallbackStatus status;  //처리상태
    private Timestamp inputDate;       //입력일시
    private Timestamp resultDate;      //처리일시
    private String queueNumber;      //큐번호
    private String ivrkey;          //ivr키
}
