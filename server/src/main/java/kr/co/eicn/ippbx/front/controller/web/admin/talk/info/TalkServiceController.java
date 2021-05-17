package kr.co.eicn.ippbx.front.controller.web.admin.talk.info;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.info.TalkServiceApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkServiceDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.TalkServiceSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.TalkServiceInfoFormRequest;
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
@RequestMapping("admin/talk/info/service")
public class TalkServiceController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkServiceController.class);

    @Autowired
    private TalkServiceApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<TalkServiceSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/talk/info/service/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TalkServiceInfoFormRequest form) {
        return "admin/talk/info/service/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") TalkServiceInfoFormRequest form) throws IOException, ResultFailException {
        final TalkServiceDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        ReflectionUtils.copy(form, entity);

        return modal(model, form);
    }
}
