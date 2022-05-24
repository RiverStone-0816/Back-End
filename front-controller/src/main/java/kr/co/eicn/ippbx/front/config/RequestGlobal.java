package kr.co.eicn.ippbx.front.config;

import kr.co.eicn.ippbx.front.model.CurrentUserMenu;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.front.service.FileService;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.util.CodeHasable;
import kr.co.eicn.ippbx.util.spring.SpringApplicationContextAware;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RequestGlobal {
    private static final String REQUEST_GLOBAL_CURRENT_USER = "REQUEST_GLOBAL_CURRENT_USER";
    private static final String REQUEST_GLOBAL_CURRENT_USER_MENUS = "REQUEST_GLOBAL_CURRENT_USER_MENUS";
    private static final String REQUEST_GLOBAL_LOGIN_INPUTS = "REQUEST_GLOBAL_LOGIN_INPUTS";
    private static final String REQUEST_GLOBAL_USING_SERVICE_LIST = "REQUEST_GLOBAL_USING_SERVICE_LIST";
    private static final String REQUEST_GLOBAL_SOCKET_LIST = "REQUEST_GLOBAL_SOCKET_LIST";
    private static final String REQUEST_GLOBAL_ALERTS = "REQUEST_GLOBAL_ALERTS";
    private static final String REQUEST_GLOBAL_SERVICE_KIND = "REQUEST_GLOBAL_SERVICE_KIND";
    private static final String REQUEST_GLOBAL_DOUB_URL = "REQUEST_GLOBAL_DOUB_URL";
    private static final String REQUEST_GLOBAL_BASE_URL = "REQUEST_GLOBAL_BASE_URL";

    private final HttpSession session;
    private final FileService fileService;

    @Value("${server.servlet.session.timeout}")
    private int sessionTimeout;

    public CurrentUserMenu getMenus() {
        return (CurrentUserMenu) session.getAttribute(REQUEST_GLOBAL_CURRENT_USER_MENUS);
    }

    public void setMenus(CurrentUserMenu menu) {
        session.setAttribute(REQUEST_GLOBAL_CURRENT_USER_MENUS, menu);
    }

    public PersonDetailResponse getUser() {
        return (PersonDetailResponse) session.getAttribute(REQUEST_GLOBAL_CURRENT_USER);
    }

    public String getSessionId() {
        return session.getId();
    }

    public LoginForm getLoginInputs() {
        return (LoginForm) session.getAttribute(REQUEST_GLOBAL_LOGIN_INPUTS);
    }

    public void setLoginInputs(LoginForm form) {
        session.setAttribute(REQUEST_GLOBAL_LOGIN_INPUTS, form);
    }

    public String getUsingServices() {
        return (String) session.getAttribute(REQUEST_GLOBAL_USING_SERVICE_LIST);
    }

    public void setUsingServices(String service) {
        session.setAttribute(REQUEST_GLOBAL_USING_SERVICE_LIST, service);
    }

    public String getServiceKind() {
        return (String) session.getAttribute(REQUEST_GLOBAL_SERVICE_KIND);
    }

    public void setServiceKind(String service) {
        session.setAttribute(REQUEST_GLOBAL_SERVICE_KIND, service);
    }

    public void setDoubUrl(String url) {
        session.setAttribute(REQUEST_GLOBAL_DOUB_URL, url);
    }

    public String getDoubUrl() {
        return (String) session.getAttribute(REQUEST_GLOBAL_DOUB_URL);
    }

    public void setBaseUrl(String url) {
        session.setAttribute(REQUEST_GLOBAL_BASE_URL, url);
    }

    public String getBaseUrl() {
        return (String) session.getAttribute(REQUEST_GLOBAL_BASE_URL);
    }

    public boolean checkLogin() {
        final PersonDetailResponse user = getUser();
        if (user == null)
            return false;

        return user.isLoginConfirmed();
    }

    public boolean isLogin() {
        return getUser() != null;
    }

    public void setCurrentUser(PersonDetailResponse user) {
        session.setMaxInactiveInterval(sessionTimeout);
        session.setAttribute(REQUEST_GLOBAL_CURRENT_USER, user);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getSocketList() {
        return (Map<String, String>) session.getAttribute(REQUEST_GLOBAL_SOCKET_LIST);
    }

    public void setSocketList(Map<String, String> list) {
        session.setAttribute(REQUEST_GLOBAL_SOCKET_LIST, list);
    }

    public void setLoginConfirm(Boolean loginConfirm) {
        final PersonDetailResponse user = getUser();
        if (user == null)
            return;

        user.setLoginConfirmed(loginConfirm);
        setCurrentUser(user);
    }

    public void invalidateSession() {
        session.invalidate();
    }

    public void alert(String code, Object... objects) {
        final List<String> alerts = getAlerts();
        alerts.add(SpringApplicationContextAware.requestMessage().getText(code, objects));
        session.setAttribute(REQUEST_GLOBAL_ALERTS, alerts);
    }

    public void alertString(String string) {
        final List<String> alerts = getAlerts();
        alerts.add(string);
        session.setAttribute(REQUEST_GLOBAL_ALERTS, alerts);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAlerts() {
        final List<String> alerts = (List<String>) session.getAttribute(REQUEST_GLOBAL_ALERTS);
        return alerts != null ? alerts : new ArrayList<>();
    }

    public List<String> popAlerts() {
        final List<String> alerts = getAlerts().stream().distinct().collect(Collectors.toList());
        session.removeAttribute(REQUEST_GLOBAL_ALERTS);
        return alerts;
    }

    public String urlEncode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }

    public String now() {
        return now("yyyy-MM-dd");
    }

    public String now(String format) {
        return dateFormat(new Date(System.currentTimeMillis()), format);
    }

    public String dateFormat(Date date, String format) {
        if (date == null)
            return null;

        final DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return dateFormat.format(date);
    }

    public String dateFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd");
    }

    public String timestampFormat(Date date, String format) {
        if (date == null)
            return null;

        final DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return dateFormat.format(date);
    }

    public String timestampFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public String escapeQuote(String text) {
        return text
                .replaceAll("'", "\\\\'")
                .replaceAll("\"", "\\\\\"")
                .replaceAll("[\n]", "\\\\n")
                .replaceAll("[\r]", "\\\\r");
    }

    public String htmlQuote(String text, String defaultStr) {
        if (StringUtils.isEmpty(text))
            return defaultStr;
        return text.replaceAll("\"", "&quot;").replaceAll("'", "&#39;").replaceAll("<", "&lt;");
    }

    public String htmlQuote(final String text) {
        return htmlQuote(text, "");
    }

    public String addQueryString(String url, String query) {
        if (!url.contains("?"))
            return url + "?" + query;
        else if (!query.startsWith("&"))
            return url + "&" + query;
        else
            return url + query;
    }

    public String url(String path) {
        return fileService.url(path);
    }

    public String padding(Number n, Integer digits) {
        return String.format("%0" + digits + "d", n);
    }

    public <C> String messageOf(String enumClassName, C code) {
        return EnumConverter.instance.messageOf(enumClassName, code);
    }

    public <E extends Enum<E> & CodeHasable<C>, C> E of(String enumClassName, C code) {
        return EnumConverter.instance.of(enumClassName, code);
    }

    public String timeFormatFromSeconds(long sec) {
        return timeFormatFromSeconds(sec, "HH:mm:ss");
    }

    public String timeFormatFromSeconds(long sec, String timeFormat) {
        final Date date = new Date(sec * 1000);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public String clientIp() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getRemoteHost();
    }

    public String timeFormatFromSecondsWithoutSimpleDateFormat(long sec) {
        long hours = sec / (60 * 60);
        long minutes = sec / 60 % 60;
        long seconds = sec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
