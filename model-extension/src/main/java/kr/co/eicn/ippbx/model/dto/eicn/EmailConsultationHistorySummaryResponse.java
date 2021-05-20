package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class EmailConsultationHistorySummaryResponse {
    private Integer id;            //아이디
    private String userName;       //분배된 사용자명
    private String userTrName;     //이관할 사용자명
    /**
     * @see kr.co.eicn.ippbx.model.enums.ResultCodeType
     */
    private String resultCodeName;      //처리여부
    private String resultServiceName;   //관련서비스
    private String resultKindName;      //상담종류
    private Timestamp sentDate;        //메일수신기간
    private String fromName;        //발신자명
    private String fromEmail;       //발신이메일
    private String subject;         //제목
    private String customName;      //고객명
    private String customCompanyName;   //고객사정보
}
