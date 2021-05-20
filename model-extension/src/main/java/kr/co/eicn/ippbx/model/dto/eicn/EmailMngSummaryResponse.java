package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class EmailMngSummaryResponse {
    private Integer seq;                 //시퀀스
    private String serviceName;          //서비스명
    private String mailUserName;         //대표메일계정
    /**
     * @see kr.co.eicn.ippbx.model.enums.MailProtocolType
     */
    private String mailProtocol;    //메일 프로토콜(SMTP, POP3, IMAP)
    private String sendEmail;            //보내는메일
    private String sendEmailName;        //보내는메일명
}
