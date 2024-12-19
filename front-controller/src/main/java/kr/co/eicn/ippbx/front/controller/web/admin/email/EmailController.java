package kr.co.eicn.ippbx.front.controller.web.admin.email;

import kr.co.eicn.ippbx.model.enums.MailProtocolType;
import kr.co.eicn.ippbx.model.enums.SendAuthConnType;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.email.EmailApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.EmailMngDetailResponse;
import kr.co.eicn.ippbx.model.form.EmailMngFormRequest;
import kr.co.eicn.ippbx.model.search.EmailMngSearchRequest;
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

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/email/email")
public class EmailController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") EmailMngSearchRequest search) throws IOException, ResultFailException {
        final Pagination<EmailMngDetailResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/email/email/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") EmailMngFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("MailProtocolTypes", FormUtils.options(MailProtocolType.class));
        model.addAttribute("SendAuthConnTypes", FormUtils.options(SendAuthConnType.class));

        return "admin/email/email/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") EmailMngFormRequest form) throws IOException, ResultFailException {
        final EmailMngDetailResponse entity = apiInterface.get(seq);
        ReflectionUtils.copy(form, entity);

        model.addAttribute("entity", entity);

        return modal(model, form);
    }
}
