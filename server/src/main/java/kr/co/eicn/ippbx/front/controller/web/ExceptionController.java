package kr.co.eicn.ippbx.front.controller.web;

import kr.co.eicn.ippbx.front.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author tinywind
 * @since 2017-05-15
 */
@Controller
@RequestMapping("exception")
public class ExceptionController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    @GetMapping("")
    public String exception(Model model, HttpServletRequest request) {
        model.addAttribute("errorInformation", "페이지를 표시할 수 없습니다.");

        if (g.getAlerts().size() == 0)
            g.alert("error.system");
        return "close-tab-page";
    }

    @GetMapping("404")
    public String pageNotFound(Model model, HttpServletRequest request) {
        model.addAttribute("errorInformation", "페이지를 표시할 수 없습니다.");

        if (g.getAlerts().size() == 0)
            g.alert("error.access.noexist");
        return "close-tab-page";
    }

    @GetMapping("401")
    public String unauthorized(Model model, HttpServletRequest request) {
        model.addAttribute("errorInformation", "페이지를 표시할 수 없습니다.");

        if (g.getAlerts().size() == 0)
            g.alert("error.access.unauthorized");
        return "exception-unauthorized";
    }

    private String getErrorMessage(HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        final Enumeration<String> names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            final String name = names.nextElement();
            if (name.contains("status_code") || !name.startsWith("javax.servlet")) continue;
            final Object attribute = request.getAttribute(name);
            builder.append(name).append(": ").append(attribute).append("\n\t");
        }

        final String message = builder.toString();
        logger.info(message);

        return message;
    }
}
