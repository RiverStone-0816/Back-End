package kr.co.eicn.ippbx.front.controller.web.admin.service.etc;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.etc.MonitApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.MonitControlResponse;
import kr.co.eicn.ippbx.server.model.enums.PersonPausedStatus;
import kr.co.eicn.ippbx.server.model.search.MonitControlSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/service/etc/license-state")
public class LicenseStateController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LicenseStateController.class);

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        return "admin/service/etc/license-state/ground";
    }

}
