package kr.co.eicn.ippbx.front.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 이제까지는 JSESSIONID를 통해 세션ID를 컨버팅해서 사용했는데(이게 스프링 기본..)
 * 다른 도메인에서 전달되는(CORS) request에는 Cookie를 지정할수 없어서
 * X-SESSION-ID 헤더를 통해서 세션ID를 전달받아 사용하려는 목적
 * <br/>
 * 이게 일반적인 상황에서는 나올수 없는데.. 스프링이랑 NODE 앱이랑 분리해서
 * 개발하는 단계에서는 스프링위에 NODE앱을 띄우기에는 너무 불편해서..
 * CORS상황에서 COOKIE를 대신하려는 목적으로 만들어둠
 * <br/>
 * 스프링의 기본 세션 동작 순서는 (설명에서 SessionRepositoryFilter 동작은 생략한다.)
 * <ol>
 *     <li>HttpSessionIdResolver.resolveSessionIds()</li>
 *     <li>SessionRepository.createSession()</li>
 *     <li>SessionRepository.save()</li>
 *     <li>HttpSessionIdResolver.setSessionId()</li>
 * </ol>
 * 기존 동작에서 수정하기 힘든까닭은 sessionId를 request를 참조해서 만들지 않는다는거.. 걍 randomId를 만들고 setSessionId로 쿠키에 찍어버림
 * <br/>
 * org.springframework.session.web.http.CookieHttpSessionIdResolver 를 확장시켰다. 이게... final이라서...
 */
@Component
public class CookieAndXHeaderHttpSessionIdResolver implements HttpSessionIdResolver {

    public static final String WRITTEN_SESSION_ID_ATTR = CookieHttpSessionIdResolver.class.getName().concat(".WRITTEN_SESSION_ID_ATTR");
    public static final String HEADER_X_SESSION_ID = "X-Auth-Token";

    private static final String cookieName = "JSESSIONID";
    private final DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
    private final CookieAndXHeaderIdSessionRepository sessionRepository;

    public <S extends Session> CookieAndXHeaderHttpSessionIdResolver(CookieAndXHeaderIdSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        cookieSerializer.setCookieName(cookieName);
        cookieSerializer.setUseBase64Encoding(false);
    }

    // todo: header 에 SESSIONID 가 없는 경우 session 를 다시 생성 함으로써 url rewriting 에 문제 발생하여 일부 수정. 해당내용은 다시 수정 될 수 있음.
    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        String jsessionid = null;
        if (request.getRequestURL().toString().split(";").length > 1)
            jsessionid = request.getRequestURL().toString().split(";")[1].split("=").length > 1 ? request.getRequestURL().toString().split(";")[1].split("=")[1] : null;

        final String headerSessionId = StringUtils.isNotEmpty(request.getHeader(HEADER_X_SESSION_ID)) ? request.getHeader(HEADER_X_SESSION_ID) : jsessionid;
        if (StringUtils.isNotEmpty(headerSessionId)) {
            final Session session = sessionRepository.findById(headerSessionId);
            if (session == null)
                sessionRepository.save(sessionRepository.createSession(headerSessionId));
            return Collections.singletonList(headerSessionId);
        }
        return this.cookieSerializer.readCookieValues(request);
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        final String headerSessionId = request.getHeader(HEADER_X_SESSION_ID);
        if (StringUtils.isNotEmpty(headerSessionId))
            return;

        if (sessionId.equals(request.getAttribute(WRITTEN_SESSION_ID_ATTR)))
            return;

        request.setAttribute(WRITTEN_SESSION_ID_ATTR, sessionId);
        this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, sessionId));
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, ""));
    }


}
