package kr.co.eicn.ippbx.front.config;

import kr.co.eicn.ippbx.front.model.CurrentUserMenu;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.spring.DateTypePropertyEditor;
import kr.co.eicn.ippbx.front.util.spring.TagExtender;
import kr.co.eicn.ippbx.server.model.dto.eicn.PersonDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author tinywind
 */
@ControllerAdvice
@PropertySource(value = {"classpath:application.properties", "classpath:maven.properties"})
public class GlobalBinder {
    @Value("${apiserver.url}")
    protected String apiServerUrl;
    @Autowired
    private TagExtender tagExtender;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RequestGlobal g;
    @Autowired
    private CachedEntity cached;
    @Autowired
    private RequestMessage requestMessage;
    @Autowired
    private HttpSession session;

    @Value("${application.version}")
    private String version;
    @Value("${application.devel:true}")
    private Boolean devel;
    @Value("${server.domain}")
    protected String serviceUrl;
    @Value("${server.servicekind}")
    private String serviceKind;
    @Value("${messenger.cipher.key}")
    private String cipherKey;

    @ModelAttribute("devel")
    public Boolean devel() {
        return devel;
    }

    @ModelAttribute("version")
    public String version() {
        return version;
    }

    @ModelAttribute("serviceUrl")
    public String serviceUrl() {
        return serviceUrl;
    }

    @ModelAttribute("cached")
    public CachedEntity cachedEntity() {
        return cached;
    }

    @ModelAttribute("cipherKey")
    public String cipherKey() {
        return cipherKey;
    }

    @ModelAttribute("g")
    public RequestGlobal requestGlobal() {
        return g;
    }

    @ModelAttribute("request")
    public HttpServletRequest request() {
        return request;
    }

    @ModelAttribute("tagExtender")
    public TagExtender tagExtender() {
        return tagExtender;
    }

    @ModelAttribute("user")
    public PersonDetailResponse user() {
        return g.getUser();
    }

    @ModelAttribute("message")
    public RequestMessage message() {
        return requestMessage;
    }

    @ModelAttribute("accessToken")
    public String accessToken() {
        return (String) session.getAttribute(ApiServerInterface.SESSION_ACCESS_TOKEN);
    }

    @ModelAttribute("apiServerUrl")
    public String apiServerUrl() {
        return apiServerUrl;
    }

    @ModelAttribute("menu")
    public CurrentUserMenu currentUserMenu() {
        return g.getMenus();
    }

    @ModelAttribute("usingServices")
    public String usingServices() {return g.getUsingServices();}

    @ModelAttribute("serviceKind")
    public String serviceKind() {
        return serviceKind;
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
