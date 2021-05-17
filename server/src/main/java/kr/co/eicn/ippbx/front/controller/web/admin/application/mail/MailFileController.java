package kr.co.eicn.ippbx.front.controller.web.admin.application.mail;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.MailFileForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.mail.MailFileApiInterface;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendFileResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.server.model.enums.SendCategoryType;
import kr.co.eicn.ippbx.server.model.search.SendCategorySearchRequest;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/application/mail/file")
public class MailFileController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MailFileController.class);

    @Autowired
    private MailFileApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") SendCategorySearchRequest search) throws IOException, ResultFailException {
        final Pagination<SendFileResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/application/mail/file/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") MailFileForm form) throws IOException, ResultFailException {
        final Map<String, String> categories = apiInterface.sendCategory().stream().collect(Collectors.toMap(SendSmsCategorySummaryResponse::getCategoryCode, SendSmsCategorySummaryResponse::getCategoryName));
        model.addAttribute("categories", categories);
        return "admin/application/mail/file/modal";
    }

    @GetMapping("{id}/modal")
    public String modal(Model model, @PathVariable Long id, @ModelAttribute("form") MailFileForm form) throws IOException, ResultFailException {
        final SendFileResponse entity = apiInterface.get(id);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        return modal(model, form);
    }
}
