package kr.co.eicn.ippbx.front.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.ArsAuthInfo;
import kr.co.eicn.ippbx.front.model.CurrentUserMenu;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.front.service.api.application.kms.KmsGraphQLInterface;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.AuthApiInterface;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.DaemonInfoInterface;
import kr.co.eicn.ippbx.front.service.api.MenuApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.PhoneApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies;
import kr.co.eicn.ippbx.model.dto.configdb.UserMenuCompanyResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoDetailResponse;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.enums.WebSecureActionType;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@Api(tags = "사용자정보", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(AuthApiController.class);

    @Autowired
    private AuthApiInterface authApiInterface;
    @Autowired
    private UserApiInterface userApiInterface;
    @Autowired
    private DaemonInfoInterface daemonInfoInterface;
    @Autowired
    private MenuApiInterface menuApiInterface;
    @Autowired
    private CompanyApiInterface companyApiInterface;
    @Autowired
    private PhoneApiInterface phoneApiInterface;
    @Autowired
    private KmsGraphQLInterface kmsGraphQLInterface;

    @Value("${eicn.admin.socket.id}")
    private String adminSocketId;
    @Value("${eicn.call-control.socket.id}")
    private String callControlSocketId;
    @Value("${eicn.talk.socket.id}")
    private String talkSocketId;
    @Value("${eicn.wtalk.socket.id}")
    private String wtalkSocketId;
    @Value("${eicn.pds.socket.id}")
    private String pdsSocketId;
    @Value("${eicn.messenger.socket.id}")
    private String messengerSocketId;
    @Value("${eicn.chatbot.socket.id}")
    private String chatbotSocketId;
    @Value("${eicn.service.servicekind}")
    private String serviceKind;
    @Value("${eicn.service.base-url}")
    private String baseUri;
    @Value("${eicn.service.base-url}")
    private String doubUri;

    @LoginRequired
    @GetMapping("access-token")
    public String getAccessToken() {
        return authApiInterface.getAccessToken();
    }

    @PostMapping("check-login-condition")
    public ArsAuthInfo checkLoginCondition(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid LoginForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        authApiInterface.login(form);

        final boolean isArs = companyApiInterface.checkService("LGN");
        if (!isArs) {
            login(form, bindingResult, request, response);

            return null;
        }

        final ArsAuthInfo arsAuth = authApiInterface.getArsAuth(form.getId());
        if (arsAuth == null || StringUtils.isEmpty(arsAuth.getAuthNum())) {
            login(form, bindingResult, request, response);

            return arsAuth;
        }

        companyApiInterface.getPBXServers().stream().findFirst().ifPresent(e -> arsAuth.setPbxHost(e.getHost()));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return arsAuth;
    }

    @SneakyThrows
    @GetMapping("sockets")
    public Map<String, String> sockets() {
        return daemonInfoInterface.getSocketList();
    }

    @ApiOperation("로그인")
    @PostMapping("login")
    public void login(@RequestBody @Valid LoginForm form, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws IOException, ResultFailException {
        g.invalidateSession();

        form.setActionType(WebSecureActionType.LOGIN.getCode());
        authApiInterface.login(form);
        PhoneInfoDetailResponse phone = null;
        final PersonDetailResponse user = userApiInterface.get(form.getId());

        final CompanyInfo companyInfo = companyApiInterface.getInfo(form.getCompany());

        if (Objects.equals(IdType.MASTER, IdType.of(user.getIdType()))) {
            user.setCompanyId(companyInfo.getCompanyId());
            user.setCompanyName(companyInfo.getCompanyName());
            user.setTelco(companyInfo.getTelco());

            if (companyInfo.getService().contains("ASTIN"))
                user.setLicenseList(user.getLicenseList().concat("ASTIN|"));
            if (companyInfo.getService().contains("BSTT"))
                user.setLicenseList(user.getLicenseList().concat("BSTT|"));
        } else {
            if (StringUtils.isNotEmpty(user.getPeer())) phone = phoneApiInterface.get(user.getPeer());
        }

        // if (isNotEmpty(form.getExtension()))
        user.setExtension(form.getExtension());
        user.setPhoneKind(phone != null ? phone.getPhoneKind() : "N");
        user.setStt(phone != null ? phone.getStt() : "N");
        user.setSoftphone(phone != null ? phone.getSoftphone() : "N");
        final List<UserMenuCompanyResponse> menus = menuApiInterface.getUserMenus(user.getId());

        CompanyServerEntity companyServerEntity = null;
        if (authApiInterface.getServer().stream().anyMatch(e -> e.getType().equals("U")))
            companyServerEntity = authApiInterface.getServer().stream().filter(e -> e.getType().equals("U")).collect(Collectors.toList()).get(0);

        g.setMenus(new CurrentUserMenu(menus));
        g.setCurrentUser(user);
        g.setLoginInputs(form);
        g.setUsingServices(companyInfo.getService());
        g.setServiceKind(serviceKind);
        g.setSocketList(daemonInfoInterface.getSocketList());
        g.setPassword(form.getPassword());
        if (Objects.nonNull(companyServerEntity))
            g.setDoubUrl(companyServerEntity.getDoubServerInfo().getDoubWebUrl() + "/api/session_link/" + g.getSessionId() + "?ipccUrl=");
        g.setBaseUrl(doubUri + "/api/user/session-check");
        g.setSSORequestSiteUrl();
        g.setSTTSocketUrl();

        if ("J|A".contains(g.getUser().getIdType()) && (g.getUsingServices().contains("ASTIN") || g.getUsingServices().contains("ASTOUT") || g.getUsingServices().contains("BSTT"))) {
            // 전체관리자 이상의 권한이 있으면서 회사가 서비스도 가지고 있음. (A조건)
            log.info("A조건 충족, recordLoginUserSessionId 실행@");
            authApiInterface.recordLoginUserSessionId();
        } else if ("M".contains(g.getUser().getIdType()) && g.getUsingServices().contains("ASTOUT") && g.getUser().getIsAstOut().equals("Y")) {
            // 상담사 이면서 ASTOUT 라이센스 가지고 있음. (B조건)
            log.info("B조건 충족, recordLoginUserSessionId 실행@");
            authApiInterface.recordLoginUserSessionId();
        } else
            log.info("충족하는 조건이 없어 record session 실행하지 않음.");

        // KMS토큰 발급 및 쿠키 저장
        // kmsGraphQLInterface.getAuthenticateWithCookie(request, response);
    }

    @ApiOperation("로그아웃")
    @GetMapping("logout")
    public void logout() {
        deniedLogin();
        g.invalidateSession();
    }

    @GetMapping("confirm-login")
    public void checkLogin() throws LoginException {
        if (g.getUser() != null)
            g.setLoginConfirm(true);
        else
            throw new LoginException("error.access.denied");
    }

    @GetMapping("denied-login")
    public void deniedLogin() {
        g.setLoginConfirm(false);
    }

    @LoginRequired
    @GetMapping("socket-info")
    public SocketIoInformation getSocketIoInformation() throws IOException, ResultFailException {
        final PersonDetailResponse user = g.getUser();
        final LoginForm loginForm = g.getLoginInputs();
        final OrganizationSummaryResponse organization = user.getCompanyTrees() != null && user.getCompanyTrees().size() > 0
                ? user.getCompanyTrees().get(user.getCompanyTrees().size() - 1)
                : new OrganizationSummaryResponse();
        final Map<String, String> socketMap = g.getSocketList();
        final ServerInfo serverInfo = companyApiInterface.pbxServerInfo();
        final boolean isMulti = companyApiInterface.checkService("MUL");

        final PhoneInfoDetailResponse phone = StringUtils.isEmpty(user.getPeer()) ? null : phoneApiInterface.get(user.getPeer());

        return SocketIoInformation.builder()
                .adminSocketUrl(socketMap.get(adminSocketId))
                .callControlSocketUrl(socketMap.get(callControlSocketId))
                .talkSocketUrl(socketMap.get(wtalkSocketId))
                .pdsSocketUrl(socketMap.get(pdsSocketId))
                .messengerSocketUrl(socketMap.get(messengerSocketId))
                .chatbotSocketUrl(socketMap.get(chatbotSocketId))
                .pbxName(serverInfo == null ? "" : serverInfo.getHost())
                .pbxHost(serverInfo == null ? "" : serverInfo.getIp())
                .userId(user.getId())
                .userName(user.getIdName())
                .password(getSHA512(getSHA512(loginForm.getPassword())+user.getSoltPw()))
                .companyId(user.getCompanyId())
                .extension(loginForm.getExtension())
                .idType(user.getIdType())
                .groupCode(organization.getGroupCode())
                .groupTreeName(organization.getGroupTreeName())
                .groupLevel(organization.getGroupLevel())
                .isMulti(isMulti ? "Y" : "N")
                .firstStatus(StringUtils.isEmpty(user.getPeer()) ? 0 : phone.getFirstStatus())
                .build();
    }

    @LoginRequired
    @GetMapping("softPhone-info")
    public SoftPhoneInformation getSoftPhoneInformation() throws IOException, ResultFailException {
        final PersonDetailResponse user = g.getUser();
        final LoginForm loginForm = g.getLoginInputs();
        final ServerInfo pbx = companyApiInterface.pbxServerInfo();
        final List<CompanyServerEntity> webrtc = companyApiInterface.webrtcServerInfo();
        ServerInformation serverInformation = ServerInformation.builder()
                .pbxServerIp(pbx.getIp())
                .pbxServerPort("5060")
                .webrtcServerIp(webrtc.get(0).getWebrtcServerInfo().getWebrtcServerIp())
                .webrtcServerPort(String.valueOf(webrtc.get(0).getWebrtcServerInfo().getWebrtcServerPort()))
                .turnServerIp(webrtc.get(0).getWebrtcServerInfo().getTurnServerIp())
                .turnServerPort(String.valueOf(webrtc.get(0).getWebrtcServerInfo().getTurnServerPort()))
                .turnUser(webrtc.get(0).getWebrtcServerInfo().getTurnUser())
                .turnSecret(webrtc.get(0).getWebrtcServerInfo().getTurnSecret())
                .build();

        //소프트폰이 아닐경우 리턴
        if (!user.getPhoneKind().equals("S") || StringUtils.isEmpty(loginForm.getExtension()))
            return SoftPhoneInformation.builder().result("ERROR").message("Invalid Request").peerNum("").peerSecret("").build();

        PhoneInfoDetailResponse phoneInfo = phoneApiInterface.get(user.getPeer());

        if (Objects.isNull(phoneInfo))
            return SoftPhoneInformation.builder().result("ERROR").message("Peer Not Found: " + user.getPeer()).peerNum(user.getPeer()).peerSecret("").build();

        SipBuddies sipBuddies = authApiInterface.getSoftPhoneAuth(user.getPeer());
        if (Objects.isNull(sipBuddies))
            return SoftPhoneInformation.builder().result("ERROR").message("Peer Not Found: " + user.getPeer()).peerNum(user.getPeer()).peerSecret(sipBuddies.getMd5secret()).build();

        if (StringUtils.isEmpty(sipBuddies.getMd5secret()))
            return SoftPhoneInformation.builder().result("ERROR").message("Peer Not Found: " + user.getPeer()).peerNum(user.getPeer()).peerSecret(sipBuddies.getMd5secret()).build();

        /**
         * todo - 소프트 폰만 쓰는 고객의 경우 해당 주석 삭제
         * */
        /*authApiInterface.update(user.getPeer(), sipBuddies.getMd5secret());
        sipBuddies = authApiInterface.getSoftPhoneAuth(user.getPeer());

        try (final InputStream in = Runtime.getRuntime().exec("ssh root@"+ pbx.getHost() +" asterisk -rx \\\\\"sip prune realtime peer "+ user.getPeer() +"\\\\\"").getInputStream();
             final InputStreamReader reader = new InputStreamReader(in);
             final BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                log.info("asterisk message[message={}]", line);
            }
        } catch (IOException e) {
            log.error("asterisk script ERROR[error={}]", e.getMessage());
            throw new IllegalArgumentException("실행 할 수 없습니다.");
        }*/

        return SoftPhoneInformation.builder().result("OK")
                .message("")
                .peerNum(user.getPeer())
                .peerSecret(sipBuddies.getMd5secret())
                .serverInformation(serverInformation)
                .build();
    }

    @Builder
    @Data
    public static class SocketIoInformation {
        private String adminSocketUrl;
        private String callControlSocketUrl;
        private String talkSocketUrl;
        private String pdsSocketUrl;
        private String messengerSocketUrl;
        private String chatbotSocketUrl;
        private String pbxName;
        private String pbxHost;
        private String userId;
        private String userName;
        private String password;
        private String companyId;
        private String extension;
        private String idType;
        private String groupCode;
        private String groupTreeName;
        private Integer groupLevel;
        private String isMulti;
        private Integer firstStatus;
    }

    @Builder
    @Data
    public static class SoftPhoneInformation {
        private String result;
        private String message;
        private String peerNum;
        private String peerSecret;
        private ServerInformation serverInformation;
    }

    @Builder
    @Data
    public static class ServerInformation {
        private String pbxServerIp;
        private String pbxServerPort;
        private String webrtcServerIp;
        private String webrtcServerPort;
        private String turnServerIp;
        private String turnServerPort;
        private String turnUser;
        private String turnSecret;
    }

    private String getSHA512(String str) {
        String rtnSHA = "";
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-512");
            sh.update(str.getBytes());
            byte[] byteData = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            rtnSHA = sb.toString();
        }catch(NoSuchAlgorithmException e){
            log.error("Exception!", e);
            rtnSHA = null;
        }
        return rtnSHA;
    }
}
