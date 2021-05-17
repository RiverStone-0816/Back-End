package kr.co.eicn.ippbx.front.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.ArsAuthInfo;
import kr.co.eicn.ippbx.front.model.CurrentUserMenu;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.AuthApiInterface;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.DaemonInfoInterface;
import kr.co.eicn.ippbx.front.service.api.MenuApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.server.model.dto.configdb.UserMenuCompanyResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.server.model.enums.IdType;
import kr.co.eicn.ippbx.server.model.enums.WebSecureActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:application.properties")
public class AuthApiController extends ApiBaseController {
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

    @Value("${admin-socket.id}")
    private String adminSocketId;
    @Value("${call-control-socket.id}")
    private String callControlSocketId;
    @Value("${talk-socket.id}")
    private String talkSocketId;
    @Value("${pds-socket.id}")
    private String pdsSocketId;
    @Value("${messenger-socket.id}")
    private String messengerSocketId;

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

    @ApiOperation("로그인")
    @PostMapping("login")
    public void login(@RequestBody @Valid LoginForm form, BindingResult bindingResult) throws IOException, ResultFailException {
        g.invalidateSession();

        form.setActionType(WebSecureActionType.LOGIN.getCode());
        authApiInterface.login(form);

        final PersonDetailResponse user = userApiInterface.get(form.getId());
        final CompanyInfo companyInfo = companyApiInterface.getInfo(form.getCompany());

        if (Objects.equals(IdType.MASTER, IdType.of(user.getIdType()))) {
            user.setCompanyId(companyInfo.getCompanyId());
            user.setCompanyName(companyInfo.getCompanyName());
        }

        // if (isNotEmpty(form.getExtension()))
        user.setExtension(form.getExtension());

        final List<UserMenuCompanyResponse> menus = menuApiInterface.getUserMenus(user.getId());

        g.setMenus(new CurrentUserMenu(menus));
        g.setCurrentUser(user);
        g.setLoginInputs(form);
        g.setUsingServices(companyInfo.getService());
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
        return new SocketIoInformation(
                socketMap.get(adminSocketId), socketMap.get(callControlSocketId), socketMap.get(talkSocketId), socketMap.get(pdsSocketId), socketMap.get(messengerSocketId),
                serverInfo == null ? "" : serverInfo.getHost(), serverInfo == null ? "" : serverInfo.getIp(),
                user.getId(), user.getIdName(), loginForm.getPassword(), user.getCompanyId(),
                loginForm.getExtension(), user.getIdType(),
                organization.getGroupCode(), organization.getGroupTreeName(), organization.getGroupLevel()
                , isMulti ? "Y" : "N"
        );
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class SocketIoInformation {
        private String adminSocketUrl;
        private String callControlSocketUrl;
        private String talkSocketUrl;
        private String pdsSocketUrl;
        private String messengerSocketUrl;
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
}
