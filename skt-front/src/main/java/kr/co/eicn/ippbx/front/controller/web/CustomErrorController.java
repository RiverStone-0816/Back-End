package kr.co.eicn.ippbx.front.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public String handleError(HttpServletResponse response, Exception e, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return "error";
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
