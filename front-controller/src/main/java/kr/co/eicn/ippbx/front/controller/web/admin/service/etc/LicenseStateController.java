package kr.co.eicn.ippbx.front.controller.web.admin.service.etc;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.AdminCheckApiInterface;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequiredArgsConstructor
@RequestMapping("admin/service/etc/license-state")
public class LicenseStateController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LicenseStateController.class);

    private final AdminCheckApiInterface adminCheckApiInterface;

    @GetMapping("")
    public String page(Model model) throws Exception {
        adminCheckApiInterface.check();
        return "admin/service/etc/license-state/ground";
    }

}
