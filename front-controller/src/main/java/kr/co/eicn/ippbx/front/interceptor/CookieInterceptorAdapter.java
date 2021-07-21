package kr.co.eicn.ippbx.front.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tinywind
 */
@Component
public class CookieInterceptorAdapter extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        try {
            final ResponseCookie cookie = ResponseCookie.from("JSESSIONID", request.getSession().getId())
                    .sameSite("Strict")
                    .secure(false)
                    .path("/")
                    .httpOnly(true)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        } catch (Exception ignored) {
        }
    }
}
