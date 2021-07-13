package kr.co.eicn.ippbx.front.config;

import kr.co.eicn.ippbx.front.model.CurrentUserMenu;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.util.spring.DateTypePropertyEditor;
import kr.co.eicn.ippbx.util.spring.RequestMessage;
import kr.co.eicn.ippbx.util.spring.TagExtender;
import kr.co.eicn.ippbx.util.spring.stroage.SessionStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author tinywind
 */
@AllArgsConstructor
@ControllerAdvice
public class GlobalBinder {

    private final TagExtender tagExtender;
    private final RequestMessage requestMessage;
    private final HttpServletRequest request;
    private final RequestGlobal g;
    private final SessionStorage storage;

    @ModelAttribute("devel")
    public Boolean devel(@Value("${eicn.debugging}") Boolean debugging) {
        return Objects.equals(debugging, true);
    }

    @ModelAttribute("version")
    public String version(@Value("${eicn.version}") String version) {
        return version;
    }

    @ModelAttribute("serviceUrl")
    public String serviceUrl(@Value("${eicn.version}") String serviceUrl) {
        return serviceUrl;
    }

    @ModelAttribute("cipherKey")
    public String cipherKey(@Value("${eicn.version}") String cipherKey) {
        return cipherKey;
    }

    @ModelAttribute("apiServerUrl")
    public String apiServerUrl(@Value("${eicn.apiserver}") String apiServerUrl) {
        return apiServerUrl;
    }

    @ModelAttribute("serviceKind")
    public String serviceKind(@Value("${eicn.debugging}") String serviceKind) {
        return serviceKind;
    }

    @ModelAttribute("accessToken")
    public String accessToken(HttpSession session) {
        return storage.get(session.getId(), ApiServerInterface.SESSION_ACCESS_TOKEN, String.class);
    }

    @ModelAttribute("contextPath")
    public String contextPath() {
        return request.getContextPath();
    }

    @ModelAttribute("menu")
    public CurrentUserMenu currentUserMenu() {
        return g.getMenus();
    }

    @ModelAttribute("usingServices")
    public String usingServices() {
        return g.getUsingServices();
    }

    @ModelAttribute("user")
    public PersonDetailResponse user() {
        return g.getUser();
    }

    @ModelAttribute("g")
    public RequestGlobal requestGlobal() {
        return g;
    }

    @ModelAttribute("tagExtender")
    public TagExtender tagExtender() {
        return tagExtender;
    }

    @ModelAttribute("message")
    public RequestMessage message() {
        return requestMessage;
    }

    @ModelAttribute("request")
    public HttpServletRequest request() {
        return request;
    }

    @InitBinder
    public void registerCustomEditors(WebDataBinder binder) {
        binder.registerCustomEditor(java.sql.Time.class, new DateTypePropertyEditor<>(java.sql.Time.class));
        binder.registerCustomEditor(java.sql.Timestamp.class, new DateTypePropertyEditor<>(java.sql.Timestamp.class, (t) -> {
            if (t == null) return null;
            return ((java.util.Date) t).getTime() + "";
        }));
        binder.registerCustomEditor(java.sql.Date.class, new DateTypePropertyEditor<>(java.sql.Date.class));
        binder.registerCustomEditor(java.util.Date.class, new DateTypePropertyEditor<>(java.util.Date.class));
    }
}
