package kr.co.eicn.ippbx.front.controller.web.admin.outbound.type;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.type.OutboundCommonCodeApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonTypeDetailResponse;
import kr.co.eicn.ippbx.server.model.enums.CommonTypeKind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/outbound/type/pds-code")
public class OutboundPdsCodeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OutboundPdsCodeController.class);

    @Autowired
    private OutboundCommonCodeApiInterface apiInterface;
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<CommonTypeDetailResponse> list = apiInterface.list(CommonTypeKind.PDS.getCode());
        model.addAttribute("list", list);
        return "admin/outbound/type/pds-code/ground";
    }
}
