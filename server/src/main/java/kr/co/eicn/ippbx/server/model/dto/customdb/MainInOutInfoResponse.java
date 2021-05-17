package kr.co.eicn.ippbx.server.model.dto.customdb;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MainInOutInfoResponse {
    private Integer   seq;
    private String    uniqueId;
    private String    inOut;   //수,발신
    private Timestamp ringDate; //수,발신 걸린 시각
    private Integer   billsec;  //통화시간
    private String    receivePath;  //수신경로
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.NormalCallStatus
     */
    private String  currencyStatus; //정상통화 여부
    private String  customNumber;   //고객번호
}
