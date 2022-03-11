package kr.co.eicn.ippbx.server.config.security;

import kr.co.eicn.ippbx.exception.EntityNotFoundException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebSecureHistoryRecord;
import kr.co.eicn.ippbx.model.UserDetails;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.enums.ServerType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionSubType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.ContextUtil;
import kr.co.eicn.ippbx.util.EicnUtils;
import kr.co.eicn.ippbx.util.EnumUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PersonListRepository personListRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final CompanyServerRepository companyServerRepository;
    private final WebSecureHistoryRepository webSecureHistoryRepository;
    private final ExtensionRepository extensionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArsAuthRepository arsAuthRepository;
    private final PBXServerInterface pbxServerInterface;

    public CustomAuthenticationProvider(PersonListRepository personListRepository, CompanyInfoRepository companyInfoRepository,
                                        CompanyServerRepository companyServerRepository, WebSecureHistoryRepository webSecureHistoryRepository
            , ExtensionRepository extensionRepository, PasswordEncoder passwordEncoder, ArsAuthRepository arsAuthRepository, PBXServerInterface pbxServerInterface) {
        this.personListRepository = personListRepository;
        this.companyInfoRepository = companyInfoRepository;
        this.companyServerRepository = companyServerRepository;
        this.webSecureHistoryRepository = webSecureHistoryRepository;
        this.extensionRepository = extensionRepository;
        this.passwordEncoder = passwordEncoder;
        this.arsAuthRepository = arsAuthRepository;
        this.pbxServerInterface = pbxServerInterface;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final CompanyIdUsernamePasswordAuthenticationToken authenticationRequest = (CompanyIdUsernamePasswordAuthenticationToken) authentication;
        final String id = authenticationRequest.getPrincipal().toString();
        UserDetails details;

        final CompanyInfo company = companyInfoRepository.findOneByCompanyId(authenticationRequest.getCompanyId());
        final PersonList user = personListRepository.findOneById(id);
        if (company == null)
            throw new IllegalStateException("해당하는 회사 정보가 없습니다.");
        if (!"C".equals(company.getStatus()))
            throw new IllegalStateException("고객사 계정상태가 사용중이 아닙니다.");

        final WebSecureHistoryRecord webSecureHistoryRecord = new WebSecureHistoryRecord();
        final HttpServletRequest request = ContextUtil.getRequest();
        final String ip = request.getHeader("HTTP_CLIENT_IP");

        if (user == null) {
            webSecureHistoryRecord.setActionId(WebSecureActionType.LOGIN.getCode());
            webSecureHistoryRecord.setActionSubId(WebSecureActionSubType.NO_USERID.getCode());
            webSecureHistoryRecord.setActionData("아이디: " + id);
            webSecureHistoryRepository.insert(webSecureHistoryRecord);
            throw new IllegalStateException("아이디 패스워드 인증에 실패하셨습니다.");
        }

        webSecureHistoryRecord.setSecureSessionid(id.toLowerCase().concat("_").concat(String.valueOf(new Date().getTime())));
        webSecureHistoryRecord.setSecureIp(StringUtils.isNotEmpty(ip) ? ip : request.getRemoteHost());
        webSecureHistoryRecord.setUserid(id);
        webSecureHistoryRecord.setUserName(user.getIdName());
        webSecureHistoryRecord.setIdType(user.getIdType());
        webSecureHistoryRecord.setCompanyId(authenticationRequest.getCompanyId());
        webSecureHistoryRecord.setInsertDate(new Timestamp(System.currentTimeMillis()));

        if (!company.getService().contains("APP") && user.getIdType().equals(IdType.USER.getCode()))
            throw new IllegalStateException("상담원 기능을 쓸수 없는 고객입니다.");

        if (user.getTryLoginCount() >= 5 && System.currentTimeMillis() - user.getTryLoginDate().getTime() <= 1000 * 60 * 5)
            throw new IllegalStateException("해당 아이디로 5회이상 로그인 실패하여 5분간 접속이 제한됩니다.\n 관리자에게 문의해주세요.");

        if (!Objects.equals(IdType.MASTER, IdType.of(user.getIdType()))) {
            if (!user.getCompanyId().equals(authenticationRequest.getCompanyId())) {
                webSecureHistoryRecord.setActionId(WebSecureActionType.LOGIN.getCode());
                webSecureHistoryRecord.setActionSubId(WebSecureActionSubType.WRONG_COMPANYID.getCode());
                webSecureHistoryRecord.setActionData("아이디: " + id);
                webSecureHistoryRepository.insert(webSecureHistoryRecord);
                throw new IllegalStateException("아이디 패스워드 인증에 실패하셨습니다.");
            }
        }

		/*if (!passwordEncoder.matches(authenticationRequest.getCredentials().toString(), user.getPasswd())) {
			webSecureHistoryRecord.setActionId("LOGIN");
			webSecureHistoryRecord.setActionSubId("WRONG_PASSWORD");
			webSecureHistoryRecord.setActionData("패스워드오류");
			webSecureHistoryRepository.insert(webSecureHistoryRecord);
			throw new IllegalStateException("아이디 패스워드 인증에 실패하셨습니다.");
		}*/

        if (!getSHA512(authenticationRequest.getCredentials().toString()).equals(user.getPasswd()) && !authenticationRequest.getCredentials().toString().equals(user.getPasswd())) {
            webSecureHistoryRecord.setActionId(WebSecureActionType.LOGIN.getCode());
            webSecureHistoryRecord.setActionSubId(WebSecureActionSubType.WRONG_PASSWORD.getCode());
            webSecureHistoryRecord.setActionData("패스워드오류");
            personListRepository.countLoginFail(user.getId(), user.getTryLoginCount() + 1);
            webSecureHistoryRepository.insert(webSecureHistoryRecord);
            throw new IllegalStateException("아이디 패스워드 인증에 실패하셨습니다.");
        }

        final Optional<CompanyServerEntity> optionalPbxServer = companyServerRepository.findAllType(ServerType.PBX).stream().findAny();

        if (!optionalPbxServer.isPresent())
            throw new IllegalStateException("PBX 서버 정보가 없습니다.");

        final PhoneInfo phone = extensionRepository.findOne(PHONE_INFO.EXTENSION.eq(authenticationRequest.getExtension()).and(PHONE_INFO.COMPANY_ID.eq(authenticationRequest.getCompanyId())));

        if (IdType.isConsultant(user.getIdType())) {
            if (StringUtils.isEmpty(authenticationRequest.getExtension()))
                throw new IllegalArgumentException("내선번호를 입력해 주세요.");
        }

        if (StringUtils.isNotEmpty(authenticationRequest.getExtension())) {
            if (phone == null) {
                webSecureHistoryRecord.setActionId(WebSecureActionType.LOGIN.getCode());
                webSecureHistoryRecord.setActionSubId(WebSecureActionSubType.NO_EXTEN.getCode());
                webSecureHistoryRecord.setActionData(authenticationRequest.getExtension());
                webSecureHistoryRepository.insert(webSecureHistoryRecord);
                throw new EntityNotFoundException("해당하는 내선이 없습니다.");
            }
        }

        if (!IdType.MASTER.getCode().equals(user.getIdType())) {
            //이중인증시휴대폰없음
            if (company.getService().contains("LGN")) {
                if (isEmpty(user.getHpNumber()) || !user.getHpNumber().startsWith("01")) {
                    webSecureHistoryRecord.setActionId(WebSecureActionType.LOGIN.getCode());
                    webSecureHistoryRecord.setActionSubId(WebSecureActionSubType.NO_PHONE_NUMBER.getCode());
                    webSecureHistoryRecord.setActionData(id);
                    webSecureHistoryRepository.insert(webSecureHistoryRecord);
                    throw new IllegalStateException("ARS인증을 위한 전화번호가 등록되지 않았습니다.");
                }

                final CompanyServerEntity pbxServer = optionalPbxServer.get();

                if (Constants.LOCAL_HOST.equals(pbxServer.getHost())) {
                    arsAuthRepository.insert(id, user.getHpNumber(), EicnUtils.getRandomNumber(), authenticationRequest.getSessionId(), authenticationRequest.getCompanyId());
                } else {
                    final DSLContext pbxDsl = pbxServerInterface.using(pbxServer.getHost());
                    arsAuthRepository.insert(pbxDsl, id, user.getHpNumber(), EicnUtils.getRandomNumber(), authenticationRequest.getSessionId(), authenticationRequest.getCompanyId());
                }
            }
        }

        details = new UserDetails(user.getId(), user.getPasswd(), Collections.singletonList(EnumUtils.of(IdType.class, user.getIdType())));

        ReflectionUtils.copy(details, user);

        if (Objects.equals(IdType.MASTER, IdType.of(user.getIdType())))
            details.setCompanyId(authenticationRequest.getCompanyId());
        if (Objects.nonNull(authenticationRequest.getExtension())) {
            details.setExtension(authenticationRequest.getExtension());
            webSecureHistoryRecord.setExtension(authenticationRequest.getExtension());
        }

        webSecureHistoryRecord.setActionId(WebSecureActionType.LOGIN.getCode());
        webSecureHistoryRecord.setActionSubId(WebSecureActionSubType.OK.getCode());
        webSecureHistoryRecord.setActionData(StringUtils.isNotEmpty(authenticationRequest.getActionType()) ? "최종로긴성공" : "로긴시도성공");

        personListRepository.countLoginFail(user.getId(), 0);
        webSecureHistoryRepository.insert(webSecureHistoryRecord);

        return new CompanyIdUsernamePasswordAuthenticationToken(details);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CompanyIdUsernamePasswordAuthenticationToken.class);
    }

    private String getSHA512(String str) {
        String rtnSHA = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            rtnSHA = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            rtnSHA = null;
        }
        return rtnSHA;
    }
}
