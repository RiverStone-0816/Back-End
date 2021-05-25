package kr.co.eicn.ippbx.front.controller.web.admin.sounds.sounds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.RingBackToneForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.sounds.RingBackToneApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.MohListSummaryResponse;
import kr.co.eicn.ippbx.model.search.MohListSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/sounds/sounds/ring-back-tone")
public class RingBackToneController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RingBackToneController.class);

    @Autowired
    private RingBackToneApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") MohListSearchRequest search) throws IOException, ResultFailException {
        final Pagination<MohListSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);
        return "admin/sounds/sounds/ring-back-tone/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") RingBackToneForm form) {
        return "admin/sounds/sounds/ring-back-tone/modal";
    }
}
