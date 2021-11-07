package kr.co.eicn.ippbx.front.controller.web.admin.application.sms;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.sms.SmsCategoryApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategoryDetailResponse;
import kr.co.eicn.ippbx.model.form.SendSmsCategoryFormRequest;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
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
@RequestMapping("admin/application/sms/category")
public class SmsCategoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SmsCategoryController.class);

    @Autowired
    private SmsCategoryApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") SendCategorySearchRequest search) throws IOException, ResultFailException {
        final Pagination<SendSmsCategoryDetailResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/application/sms/category/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") SendSmsCategoryFormRequest form) throws IOException, ResultFailException {
        return "admin/application/sms/category/modal";
    }

    @GetMapping("{categoryCode}/modal")
    public String modal(Model model, @PathVariable String categoryCode, @ModelAttribute("form") SendSmsCategoryFormRequest form) throws IOException, ResultFailException {
        final SendSmsCategoryDetailResponse entity = apiInterface.get(categoryCode);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        return modal(model, form);
    }
}
