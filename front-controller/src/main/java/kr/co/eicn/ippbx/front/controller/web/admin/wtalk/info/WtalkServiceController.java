package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.info;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.wtalk.info.WtalkServiceApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkServiceSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/wtalk/info/service")
public class WtalkServiceController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkServiceController.class);

    @Autowired
    private WtalkServiceApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<WtalkServiceSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/wtalk/info/service/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TalkServiceInfoFormRequest form) {
        return "admin/wtalk/info/service/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") TalkServiceInfoFormRequest form) throws IOException, ResultFailException {
        final WtalkServiceDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        ReflectionUtils.copy(form, entity);

        return modal(model, form);
    }
}
