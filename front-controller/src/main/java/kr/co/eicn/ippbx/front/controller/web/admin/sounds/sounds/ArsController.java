package kr.co.eicn.ippbx.front.controller.web.admin.sounds.sounds;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.ArsForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.sounds.ArsApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SoundListSummaryResponse;
import kr.co.eicn.ippbx.model.search.SoundListSearchRequest;
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
@RequestMapping("admin/sounds/sounds/ars")
public class ArsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ArsController.class);

    @Autowired
    private ArsApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") SoundListSearchRequest search) throws IOException, ResultFailException {
        final Pagination<SoundListSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/sounds/sounds/ars/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ArsForm form) {
        return "admin/sounds/sounds/ars/modal";
    }
}
