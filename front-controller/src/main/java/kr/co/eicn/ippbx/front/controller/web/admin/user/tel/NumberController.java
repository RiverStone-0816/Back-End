package kr.co.eicn.ippbx.front.controller.web.admin.user.tel;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.user.tel.NumberApiInterface;
import kr.co.eicn.ippbx.model.enums.NumberType;
import kr.co.eicn.ippbx.model.search.NumberSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/user/tel/number")
public class NumberController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(NumberController.class);

    @Autowired
    private NumberApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @RequestParam(value = "type", required = false, defaultValue = "SERVICE") NumberType type) throws IOException, ResultFailException {
        final NumberSearchRequest search = new NumberSearchRequest();
        search.setType(type.getCode());
        model.addAttribute("numbers", apiInterface.list(search));
        model.addAttribute("type", type);

        return "admin/user/tel/number/ground";
    }
}
