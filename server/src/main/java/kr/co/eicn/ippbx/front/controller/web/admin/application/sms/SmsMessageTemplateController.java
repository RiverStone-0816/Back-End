package kr.co.eicn.ippbx.front.controller.web.admin.application.sms;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsMessageTemplateApiInterface;
import kr.co.eicn.ippbx.front.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendMessageTemplateResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.SendMessageTemplateFormRequest;
import kr.co.eicn.ippbx.server.model.search.SendMessageTemplateSearchRequest;
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
@RequestMapping("admin/application/sms/message-template")
public class SmsMessageTemplateController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SmsMessageTemplateController.class);

    @Autowired
    private SmsMessageTemplateApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") SendMessageTemplateSearchRequest search) throws IOException, ResultFailException {
        final Pagination<SendMessageTemplateResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/application/sms/message-template/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") SendMessageTemplateFormRequest form) throws IOException, ResultFailException {
        final Map<String, String> categories = apiInterface.sendCategory().stream().collect(Collectors.toMap(SendSmsCategorySummaryResponse::getCategoryCode, SendSmsCategorySummaryResponse::getCategoryName));
        model.addAttribute("categories", categories);
        return "admin/application/sms/message-template/modal";
    }

    @GetMapping("{id}/modal")
    public String modal(Model model, @PathVariable Integer id, @ModelAttribute("form") SendMessageTemplateFormRequest form) throws IOException, ResultFailException {
        final SendMessageTemplateResponse entity = apiInterface.get(id);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        final Map<String, String> categories = apiInterface.sendCategory().stream().collect(Collectors.toMap(SendSmsCategorySummaryResponse::getCategoryCode, SendSmsCategorySummaryResponse::getCategoryName));
        model.addAttribute("categories", categories);

        if (!categories.containsKey(entity.getCategoryCode()))
            categories.put(entity.getCategoryCode(), entity.getCategoryName());

        return "admin/application/sms/message-template/modal";
    }
}
