package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.group;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkGroupAutoCommentApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMentDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkMentSummaryResponse;
import kr.co.eicn.ippbx.model.form.TalkMentFormRequest;
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
@RequestMapping("admin/wtalk/group/auto-comment")
public class WtalkAutoCommentController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkAutoCommentController.class);

    @Autowired
    private WtalkGroupAutoCommentApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model) throws IOException, ResultFailException {
        final List<WtalkMentSummaryResponse> list = apiInterface.list();
        model.addAttribute("list", list);

        return "admin/wtalk/group/auto-comment/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TalkMentFormRequest form) {
        return "admin/wtalk/group/auto-comment/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") TalkMentFormRequest form) throws IOException, ResultFailException {
        final WtalkMentDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        ReflectionUtils.copy(form, entity);

        return modal(model, form);
    }
}
