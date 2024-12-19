package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.model.enums.SendAuthConnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailMngDetailResponse extends EmailMngSummaryResponse {
    private String           mailUserPasswd;        // 수신 접속 계정 비밀번호
    private String           mailErrorNoticeEmail;  // 에러 알림 메일
    private String           mailViewEmail;         // 수신 접속 계정과 동일
    private Bool             mailSslYn;             // 메일 SSL 여부
    private String           mailHost;              // 수신 메일 호스트
    private Integer          mailPort;              // 수신 메일 포트
    private String           mailAttachPath;        // 첨부 저장 경로 (회사별 고정)
    private String           sendUserName;          // 발신 접속 계정
    private String           sendUserPasswd;        // 발신 접속 계정 비밀번호
    private String           sendHost;              // 발신 메일 호스트
    private Integer          sendPort;              // 발신 메일 포트
    private SendAuthConnType sendAuthConnType;      // 암호화 연결방식
}
