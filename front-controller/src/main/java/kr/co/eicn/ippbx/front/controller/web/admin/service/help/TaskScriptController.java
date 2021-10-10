package kr.co.eicn.ippbx.front.controller.web.admin.service.help;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.TaskScriptCategoryForm;
import kr.co.eicn.ippbx.front.model.form.TaskScriptForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.service.help.TaskScriptApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptCategoryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TaskScriptSummaryResponse;
import kr.co.eicn.ippbx.model.search.TaskScriptSearchRequest;
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
@RequestMapping("admin/service/help/task-script")
public class TaskScriptController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskScriptController.class);

    @Autowired
    private TaskScriptApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TaskScriptSearchRequest search) throws IOException, ResultFailException {
        final Pagination<TaskScriptSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<Long, String> categories = apiInterface.taskScriptCategoryList().stream().collect(Collectors.toMap(TaskScriptCategoryResponse::getCategoryId, TaskScriptCategoryResponse::getCategoryName));
        model.addAttribute("categories", categories);

        return "admin/service/help/task-script/ground";
    }

    @GetMapping("modal-search")
    public String modal(Model model, @ModelAttribute("search") TaskScriptSearchRequest search) throws IOException, ResultFailException {
        return "admin/service/help/task-script/modal-search";
    }

    @GetMapping("modal-search-body")
    public String modalBody(Model model, @ModelAttribute("search") TaskScriptSearchRequest search) throws IOException, ResultFailException {
        final Pagination<TaskScriptSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<Long, String> categories = apiInterface.taskScriptCategoryList().stream().collect(Collectors.toMap(TaskScriptCategoryResponse::getCategoryId, TaskScriptCategoryResponse::getCategoryName));
        model.addAttribute("categories", categories);

        return "admin/service/help/task-script/modal-search-body";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") TaskScriptForm form) throws IOException, ResultFailException {
        final Map<Long, String> categories = apiInterface.taskScriptCategoryList().stream().collect(Collectors.toMap(TaskScriptCategoryResponse::getCategoryId, TaskScriptCategoryResponse::getCategoryName));
        model.addAttribute("categories", categories);

        return "admin/service/help/task-script/modal";
    }

    @GetMapping("{id}/modal")
    public String modal(Model model, @PathVariable Long id, @ModelAttribute("form") TaskScriptForm form) throws IOException, ResultFailException {
        final TaskScriptDetailResponse entity = apiInterface.get(id);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        return modal(model, form);
    }

    @GetMapping("modal-category")
    public String modalCategory(Model model, @ModelAttribute("form") TaskScriptCategoryForm form) throws IOException, ResultFailException {
        final Map<Long, String> categories = apiInterface.taskScriptCategoryList().stream().collect(Collectors.toMap(TaskScriptCategoryResponse::getCategoryId, TaskScriptCategoryResponse::getCategoryName));
        model.addAttribute("categories", categories);

        return "admin/service/help/task-script/modal-category";
    }

    @GetMapping("{id}/modal-show")
    public String modal(Model model, @PathVariable Long id) throws IOException, ResultFailException {
        final TaskScriptDetailResponse entity = apiInterface.get(id);
        model.addAttribute("entity", entity);

        return "admin/service/help/task-script/modal-show";
    }
}
