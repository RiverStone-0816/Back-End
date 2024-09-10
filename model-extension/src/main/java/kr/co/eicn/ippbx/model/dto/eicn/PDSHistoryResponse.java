package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PDSHistoryResponse {
    private String    executeId;      //실행아이디
    private String    executeName;    //실행명
    private String    pdsName;        //pds 그룹명
    private Timestamp startDate;      //실행시작시간
    private Timestamp stopDate;       //실행종료시간
    private Integer   totalRunTime;   //수행시간
    private String    billingData;    //과금번호
    private Integer   totalCnt;       //고객수
    private Integer   numbersCnt;     //전화번호수
    private Integer   callTryCnt;     //진행된건
    private Integer   noCallCnt;      //남은건
    private String    lastCallStat;   //수신/비수신/통화중/비연결/기타

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupExecuteStatus
     **/
    private String pdsStatus;       //상태
}
