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
    @NotNull("대표메일계정")
    private String mailUserName;
    @NotNull("메일계정비밀번호")
    private String mailUserPasswd;
    @NotNull("에러시알람메일계정")
    private String mailErrorNoticeEmail;
    @NotNull("보고자하는 메일계정")
    private String mailViewEmail;
    @NotNull("메일 프로토콜(SMTP, POP3, IMAP)")
    private MailProtocolType mailProtocol;
    @NotNull("메일SSL여부(Y:사용, N:비사용)")
    private Bool mailSslYn;
    @NotNull("메일호스트")
    private String mailHost;
    @NotNull("메일포트")
    private Integer mailPort;
    @NotNull("첨부저장경로")
    private String mailAttachPath;
    @NotNull("보내는메일계정")
    private String sendUserName;
    @NotNull("보내는메일계정비밀번호")
    private String sendUserPasswd;
    @NotNull("보내는메일")
    private String sendEmail;
    @NotNull("보내는메일명")
    private String sendEmailName;
    @NotNull("보내는메일호스트")
    private String sendHost;
    @NotNull("메일포트")
    private Integer sendPort;
    @NotNull("암호화된연결방식(TLS, SSL)")
    private SendAuthConnType sendAuthConnType;

    private String companyId;

    @Override
    public boolean validate(BindingResult bindingResult) {
        if (isNotEmpty(mailHost) && isNotEmpty(sendHost)) {
            if (!PatternUtils.isIp(mailHost))
                reject(bindingResult, "mailHost", "{아이피 형식에 맞게 입력해주세요.}");
            if (!PatternUtils.isIp(sendHost))
                reject(bindingResult, "sendHost", "{아이피 형식에 맞게 입력해주세요.}");
        }

        if (isNotEmpty(mailUserName) && isNotEmpty(mailErrorNoticeEmail) && isNotEmpty(mailViewEmail) && isNotEmpty(sendEmail)) {
            if (!PatternUtils.isEmail(mailUserName))
                reject(bindingResult, "mailUserName", "{이메일 형식에 맞게 입력해주세요.}");
            if (!PatternUtils.isEmail(mailErrorNoticeEmail))
                reject(bindingResult, "mailErrorNoticeEmail", "{이메일 형식에 맞게 입력해주세요.}");
            if (!PatternUtils.isEmail(mailViewEmail))
                reject(bindingResult, "mailViewEmail", "{이메일 형식에 맞게 입력해주세요.}");
            if (!PatternUtils.isEmail(sendEmail))
                reject(bindingResult, "sendEmail", "{이메일 형식에 맞게 입력해주세요.}");
        }

        return super.validate(bindingResult);
    }
}
