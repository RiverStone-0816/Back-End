package kr.co.eicn.ippbx.front.controller.web.admin.service.help;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.NoticeForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.help.NoticeApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.BoardSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.NoticeDetailResponse;
import kr.co.eicn.ippbx.model.enums.NoticeType;
import kr.co.eicn.ippbx.model.search.BoardSearchRequest;
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

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/service/help/notice")
public class NoticeController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") BoardSearchRequest search) throws IOException, ResultFailException {
        final Pagination<BoardSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/service/help/notice/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") NoticeForm form) throws IOException, ResultFailException {
        final Map<String, String> noticeTypes = FormUtils.optionsOfCode(NoticeType.class);
        model.addAttribute("noticeTypes", noticeTypes);

        return "admin/service/help/notice/modal";
    }

    @GetMapping("{id}/modal")
    public String modal(Model model, @PathVariable Long id, @ModelAttribute("form") NoticeForm form) throws IOException, ResultFailException {
        final NoticeDetailResponse entity = apiInterface.get(id);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        return modal(model, form);
    }

    @GetMapping("{id}/modal-show")
    public String modal(Model model, @PathVariable Long id) throws IOException, ResultFailException {
        final NoticeDetailResponse entity = apiInterface.getDetail(id);
        model.addAttribute("entity", entity);

        return "admin/service/help/notice/modal-show";
    }
}
