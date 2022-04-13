package kr.co.eicn.ippbx.front.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.ArsAuthInfo;
import kr.co.eicn.ippbx.front.model.CurrentUserMenu;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author tinywind
 */
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

    @Value("${eicn.admin.socket.id}")
    private String adminSocketId;
    @Value("${eicn.call-control.socket.id}")
    private String callControlSocketId;
    @Value("${eicn.talk.socket.id}")
    private String talkSocketId;
    @Value("${eicn.pds.socket.id}")
    private String pdsSocketId;
    @Value("${eicn.messenger.socket.id}")
    private String messengerSocketId;
    @Value("${eicn.chatbot.socket.id}")
    private String chatbotSocketId;
    @Value("${eicn.service.servicekind}")
    private String serviceKind;

    @LoginRequired
    @GetMapping("access-token")
    public String getAccessToken() {
        return authApiInterface.getAccessToken();
    }

    @PostMapping("check-login-condition")
    public ArsAuthInfo checkLoginCondition(HttpServletResponse response, @RequestBody @Valid LoginForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        authApiInterface.login(form);
        final ArsAuthInfo arsAuth = authApiInterface.getArsAuth(form.getId());
        final boolean isArs = companyApiInterface.checkService("LGN");

        if (!isArs || arsAuth == null || StringUtils.isEmpty(arsAuth.getAuthNum())) {
            login(form, bindingResult);

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
    public void login(@RequestBody @Valid LoginForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        g.invalidateSession();

        form.setActionType(WebSecureActionType.LOGIN.getCode());
        authApiInterface.login(form);
        PhoneInfoDetailResponse phone = null;
        final PersonDetailResponse user = userApiInterface.get(form.getId());

        final CompanyInfo companyInfo = companyApiInterface.getInfo(form.getCompany());

        if (Objects.equals(IdType.MASTER, IdType.of(user.getIdType()))) {
            user.setCompanyId(companyInfo.getCompanyId());
            user.setCompanyName(companyInfo.getCompanyName());
        } else {
            if (StringUtils.isNotEmpty(user.getPeer()))
                phone = phoneApiInterface.get(user.getPeer());
        }

        // if (isNotEmpty(form.getExtension()))
        user.setExtension(form.getExtension());
        user.setPhoneKind(phone != null ? phone.getPhoneKind() : "N");
        final List<UserMenuCompanyResponse> menus = menuApiInterface.getUserMenus(user.getId());

        g.setMenus(new CurrentUserMenu(menus));
        g.setCurrentUser(user);
        g.setLoginInputs(form);
        g.setUsingServices(companyInfo.getService());
        g.setServiceKind(serviceKind);
        g.setSocketList(daemonInfoInterface.getSocketList());
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

        return SocketIoInformation.builder()
                .adminSocketUrl(socketMap.get(adminSocketId))
                .callControlSocketUrl(socketMap.get(callControlSocketId))
                .talkSocketUrl(socketMap.get(talkSocketId))
                .pdsSocketUrl(socketMap.get(pdsSocketId))
                .messengerSocketUrl(socketMap.get(messengerSocketId))
                .chatbotSocketUrl(socketMap.get(chatbotSocketId))
                .pbxName(serverInfo == null ? "" : serverInfo.getHost())
                .pbxHost(serverInfo == null ? "" : serverInfo.getIp())
                .userId(user.getId())
                .userName(user.getIdName())
                .password(loginForm.getPassword())
                .companyId(user.getCompanyId())
                .extension(loginForm.getExtension())
                .idType(user.getIdType())
                .groupCode(organization.getGroupCode())
                .groupTreeName(organization.getGroupTreeName())
                .groupLevel(organization.getGroupLevel())
                .isMulti(isMulti ? "Y" : "N")
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

        authApiInterface.update(user.getPeer(), sipBuddies.getMd5secret());
        sipBuddies = authApiInterface.getSoftPhoneAuth(user.getPeer());

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
}
