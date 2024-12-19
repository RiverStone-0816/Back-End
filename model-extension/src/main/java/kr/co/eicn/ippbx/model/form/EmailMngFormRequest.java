package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.model.enums.Bool;
import kr.co.eicn.ippbx.model.enums.MailProtocolType;
import kr.co.eicn.ippbx.model.enums.SendAuthConnType;
import kr.co.eicn.ippbx.util.PatternUtils;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailMngFormRequest extends BaseForm {
    @NotNull("서비스명")
    private String serviceName;

    @NotNull("수신 접속 계정")
    private String           mailUserName;
    @NotNull("수신 접속 계정 비밀번호")
    private String           mailUserPasswd;
    @NotNull("수신 메일 프로토콜")
    private MailProtocolType mailProtocol;
    @NotNull("메일 SSL 여부")
    private Bool             mailSslYn;
    @NotNull("수신 메일 호스트")
    private String           mailHost;
    @NotNull("수신 메일 포트")
    private Integer          mailPort;
    @NotNull("에러 알림 메일")
    private String           mailErrorNoticeEmail;
    private String           mailViewEmail;
    private String           mailAttachPath;        // 첨부 저장 경로 (회사별 고정)

    @NotNull("발신 접속 계정")
    private String           sendUserName;
    @NotNull("발신 접속 계정 비밀번호")
    private String           sendUserPasswd;
    @NotNull("발신 시 메일")
    private String           sendEmail;
    @NotNull("발신 시 메일명")
    private String           sendEmailName;
    @NotNull("발신 메일 호스트")
    private String           sendHost;
    @NotNull("발신 메일 포트")
    private Integer          sendPort;
    @NotNull("암호화 연결방식")
    private SendAuthConnType sendAuthConnType;

    private String companyId;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(mailHost)) {
            if (!PatternUtils.isIpOrDomain(mailHost))
                reject(bindingResult, "mailHost", "{수신 메일 호스트 형식을 확인해주세요.}");
        }

        if (isNotEmpty(sendHost)) {
            if (!PatternUtils.isIpOrDomain(sendHost))
                reject(bindingResult, "sendHost", "{발신 메일 호스트 형식을 확인해주세요.}");
        }

        if (isNotEmpty(mailUserName) && !PatternUtils.isEmail(mailUserName))
            reject(bindingResult, "mailUserName", "{`수신 접속 계정`을 이메일 형식에 맞게 입력해주세요.}");

        if (isNotEmpty(mailErrorNoticeEmail) && !PatternUtils.isEmail(mailErrorNoticeEmail))
            reject(bindingResult, "mailErrorNoticeEmail", "{`에러 알림 메일`을 이메일 형식에 맞게 입력해주세요.}");

        if (isNotEmpty(sendUserName) && !PatternUtils.isEmail(sendUserName))
            reject(bindingResult, "sendUserName", "{`발신 접속 계정`을 이메일 형식에 맞게 입력해주세요.}");

        if (isNotEmpty(sendEmail) && !PatternUtils.isEmail(sendEmail))
            reject(bindingResult, "sendEmail", "{`발신 시 메일`을 이메일 형식에 맞게 입력해주세요.}");

        return super.validate(bindingResult);
    }
}
