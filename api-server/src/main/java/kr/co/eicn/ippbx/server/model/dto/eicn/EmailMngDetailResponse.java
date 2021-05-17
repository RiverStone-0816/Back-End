package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.enums.Bool;
import kr.co.eicn.ippbx.server.model.enums.SendAuthConnType;
import lombok.Data;

@Data
public class EmailMngDetailResponse extends EmailMngSummaryResponse{
    private String mailUserPasswd;       //메일계정비밀번호
    private String mailErrorNoticeEmail; //에러시알람메일계정
    private String mailViewEmail;        //보고자하는 메일계정
    private Bool mailSslYn;              //메일SSL여부(Y:사용, N:비사용)
    private String mailHost;             //메일호스트
    private Integer mailPort;            //메일포트
    private String mailAttachPath;       //첨부저장경로
    private String sendUserName;         //보내는메일계정
    private String sendUserPasswd;       //보내는메일계정 비밀번호
    private String sendHost;             //보내는메일호스트
    private Integer sendPort;            //메일포트
    private SendAuthConnType sendAuthConnType;     //암호화된연결방식(TLS, SSL)
}
