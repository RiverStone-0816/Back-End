package kr.co.eicn.ippbx.front.controller;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.config.RequestMessage;
import kr.co.eicn.ippbx.front.util.UnauthorizedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tinywind
 */
public abstract class BaseController {

    @Autowired
    protected RequestGlobal g;

    @Autowired
    protected RequestMessage message;

    protected String redirectMain() {
        return "redirect:/";
    }

    protected String closingPopup() {
        return "closing-popup";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public void unauthorizedException(HttpServletResponse response, UnauthorizedException e, HttpServletRequest request) throws IOException {
        g.invalidateSession();
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
                "<!doctype html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta http-equiv=\"refresh\" content=\"0;url=" + request.getContextPath() + "\" data-state=\"false\">\n" +
                        "    <title>Redirecting</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "</body>\n" +
                        "</html>"
        );

        g.alertString(StringUtils.isNotEmpty(e.getMessage()) ? e.getMessage() : "허가되지 않은 접근");
    }
}
