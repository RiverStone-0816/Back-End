package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.MailProtocolType;
import lombok.Data;

@Data
public class EmailMngSummaryResponse {
    private Integer          seq;            // 고유키
    private String           serviceName;    // 서비스명
    private String           mailUserName;   // 수신 접속 계정
    private MailProtocolType mailProtocol;   // 수신 메일 프로토콜
    private String           sendEmail;      // 발신 시 메일
    private String           sendEmailName;  // 발신 시 메일명
}
